package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.StockoutRiskRow;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public InventoryAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockoutRiskRow> fetchStockoutRisk(AnalyticsPagedRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.product_id, p.sku, p.product_name,
                       COALESCE(MAX(fi.closing_stock_qty),0) AS available_qty,
                       COALESCE(MAX(fi.inbound_qty),0) AS inbound_qty,
                       COALESCE(MAX(fi.reserved_qty),0) AS reserved_qty,
                       COALESCE(AVG(fs.units_sold),0) AS avg_daily_sales,
                       COALESCE(MAX(fi.days_of_cover),0) AS days_of_cover,
                       CASE
                           WHEN bool_or(fi.out_of_stock_flag) THEN 'CRITICAL'
                           WHEN bool_or(fi.low_stock_flag) OR COALESCE(MAX(fi.days_of_cover),0) < 7 THEN 'HIGH'
                           WHEN COALESCE(MAX(fi.days_of_cover),0) < 14 THEN 'MEDIUM'
                           ELSE 'LOW'
                       END AS risk_level
                FROM ama.fact_inventory_daily fi
                LEFT JOIN ama.fact_sales_daily fs ON fs.tenant_id = fi.tenant_id AND fs.account_id = fi.account_id AND fs.product_id = fi.product_id AND fs.date_key = fi.date_key
                JOIN ama.product p ON p.product_id = fi.product_id
                JOIN ama.dim_date dd ON dd.date_key = fi.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fi", "p");
        sql.append(" GROUP BY p.product_id, p.sku, p.product_name ORDER BY CASE risk_level WHEN 'CRITICAL' THEN 1 WHEN 'HIGH' THEN 2 WHEN 'MEDIUM' THEN 3 ELSE 4 END, days_of_cover ASC ");
        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset ");
            params.put("limit", request.getPagination().sizeOrDefault());
            params.put("offset", request.getPagination().pageOrDefault() * request.getPagination().sizeOrDefault());
        }
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new StockoutRiskRow(
                rs.getLong("product_id"), rs.getString("sku"), rs.getString("product_name"), rs.getInt("available_qty"), rs.getInt("inbound_qty"), rs.getInt("reserved_qty"), rs.getDouble("avg_daily_sales"), rs.getDouble("days_of_cover"), rs.getString("risk_level")
        ));
    }
}
