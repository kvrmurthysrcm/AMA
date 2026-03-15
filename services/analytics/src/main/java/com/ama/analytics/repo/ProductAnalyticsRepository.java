package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.ProductPerformanceRow;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductPerformanceRow> fetchPerformance(AnalyticsPagedRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.product_id, p.sku, p.asin, p.product_name,
                       COALESCE(SUM(fs.net_sales_amount),0) AS net_sales,
                       COALESCE(SUM(fs.units_sold),0) AS units_sold,
                       COALESCE(SUM(ft.sessions_count),0) AS sessions,
                       COALESCE(SUM(fa.spend_amount),0) AS ad_spend,
                       CASE WHEN COALESCE(SUM(fa.spend_amount),0) = 0 THEN 0 ELSE COALESCE(SUM(fa.ad_sales_amount),0) / NULLIF(SUM(fa.spend_amount),0) END AS roas,
                       COALESCE(MAX(fi.closing_stock_qty),0) AS closing_stock_qty,
                       COALESCE(SUM(ff.estimated_profit_amount),0) AS estimated_profit
                FROM ama.product p
                LEFT JOIN ama.fact_sales_daily fs ON fs.product_id = p.product_id
                LEFT JOIN ama.fact_traffic_daily ft ON ft.tenant_id = fs.tenant_id AND ft.account_id = fs.account_id AND ft.product_id = fs.product_id AND ft.date_key = fs.date_key
                LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = fs.tenant_id AND fa.account_id = fs.account_id AND fa.product_id = fs.product_id AND fa.date_key = fs.date_key
                LEFT JOIN ama.fact_inventory_daily fi ON fi.tenant_id = fs.tenant_id AND fi.account_id = fs.account_id AND fi.product_id = fs.product_id AND fi.date_key = fs.date_key
                LEFT JOIN ama.fact_finance_daily ff ON ff.tenant_id = fs.tenant_id AND ff.account_id = fs.account_id AND ff.product_id = fs.product_id AND ff.date_key = fs.date_key
                LEFT JOIN ama.dim_date dd ON dd.date_key = fs.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fs", "p");
        sql.append(" GROUP BY p.product_id, p.sku, p.asin, p.product_name ORDER BY net_sales DESC ");
        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset ");
            params.put("limit", request.getPagination().sizeOrDefault());
            params.put("offset", request.getPagination().pageOrDefault() * request.getPagination().sizeOrDefault());
        }
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new ProductPerformanceRow(
                rs.getLong("product_id"), rs.getString("sku"), rs.getString("asin"), rs.getString("product_name"), rs.getDouble("net_sales"), rs.getLong("units_sold"), rs.getLong("sessions"), rs.getDouble("ad_spend"), rs.getDouble("roas"), rs.getInt("closing_stock_qty"), rs.getDouble("estimated_profit")
        ));
    }
}
