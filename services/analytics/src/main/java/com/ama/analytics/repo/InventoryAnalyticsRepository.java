package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.StockoutRiskRow;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InventoryAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public InventoryAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockoutRiskRow> fetchStockoutRisk(AnalyticsPagedRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("tenantId", request.getTenantId())
                .addValue("startDate", request.getDateRange().startDate())
                .addValue("endDate", request.getDateRange().endDate());

        StringBuilder sql = new StringBuilder("""
                SELECT *
                FROM (
                    SELECT p.product_id,
                           p.sku,
                           p.product_name,
                           COALESCE(MAX(fi.closing_stock_qty), 0) AS available_qty,
                           COALESCE(MAX(fi.inbound_qty), 0) AS inbound_qty,
                           COALESCE(MAX(fi.reserved_qty), 0) AS reserved_qty,
                           COALESCE(AVG(fs.units_sold), 0) AS avg_daily_sales,
                           COALESCE(MAX(fi.days_of_cover), 0) AS days_of_cover,
                           CASE
                               WHEN bool_or(fi.out_of_stock_flag) THEN 'CRITICAL'
                               WHEN bool_or(fi.low_stock_flag) OR COALESCE(MAX(fi.days_of_cover), 0) < 7 THEN 'HIGH'
                               WHEN COALESCE(MAX(fi.days_of_cover), 0) < 14 THEN 'MEDIUM'
                               ELSE 'LOW'
                           END AS risk_level
                    FROM ama.fact_inventory_daily fi
                    LEFT JOIN ama.fact_sales_daily fs
                      ON fs.tenant_id = fi.tenant_id
                     AND fs.account_id = fi.account_id
                     AND fs.product_id = fi.product_id
                     AND fs.date_key = fi.date_key
                    JOIN ama.product p
                      ON p.product_id = fi.product_id
                    JOIN ama.dim_date dd
                      ON dd.date_key = fi.date_key
                    WHERE fi.tenant_id = :tenantId
                      AND dd.full_date BETWEEN :startDate AND :endDate
                """);

        if (request.getAccountIds() != null && !request.getAccountIds().isEmpty()) {
            sql.append(" AND fi.account_id IN (:accountIds) ");
            params.addValue("accountIds", request.getAccountIds());
        }

        if (request.getMarketplaceCodes() != null && !request.getMarketplaceCodes().isEmpty()) {
            sql.append(" AND fi.marketplace_code IN (:marketplaceCodes) ");
            params.addValue("marketplaceCodes", request.getMarketplaceCodes());
        }

        if (request.getBrandIds() != null && !request.getBrandIds().isEmpty()) {
            sql.append(" AND p.brand_id IN (:brandIds) ");
            params.addValue("brandIds", request.getBrandIds());
        }

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            sql.append(" AND p.category_id IN (:categoryIds) ");
            params.addValue("categoryIds", request.getCategoryIds());
        }

        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            sql.append(" AND p.product_id IN (:productIds) ");
            params.addValue("productIds", request.getProductIds());
        }

        if (request.getSearchText() != null && !request.getSearchText().isBlank()) {
            sql.append(" AND (LOWER(p.product_name) LIKE :searchText OR LOWER(p.sku) LIKE :searchText) ");
            params.addValue("searchText", "%" + request.getSearchText().trim().toLowerCase() + "%");
        }

        sql.append("""
                    GROUP BY p.product_id, p.sku, p.product_name
                ) x
                ORDER BY
                    CASE x.risk_level
                        WHEN 'CRITICAL' THEN 1
                        WHEN 'HIGH' THEN 2
                        WHEN 'MEDIUM' THEN 3
                        ELSE 4
                    END,
                    x.days_of_cover ASC
                """);

        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset ");
            int size = request.getPagination().sizeOrDefault();
            int page = request.getPagination().pageOrDefault();
            params.addValue("limit", size);
            params.addValue("offset", page * size);
        }

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new StockoutRiskRow(
                rs.getLong("product_id"),
                rs.getString("sku"),
                rs.getString("product_name"),
                rs.getInt("available_qty"),
                rs.getInt("inbound_qty"),
                rs.getInt("reserved_qty"),
                rs.getDouble("avg_daily_sales"),
                rs.getDouble("days_of_cover"),
                rs.getString("risk_level")
        ));
    }
}
