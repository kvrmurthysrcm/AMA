package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.ProductSalesRow;
import com.ama.analytics.dto.response.SalesTimeSeriesPoint;
import com.ama.analytics.query.FilterSqlBuilder;
import com.ama.common.api.ApiMeta;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SalesAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SalesAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SalesTimeSeriesPoint> findSalesTimeseries(AnalyticsFilterRequest request) {
        String bucketExpr = switch (request.getGranularity() == null ? "DAY" : request.getGranularity().toUpperCase()) {
            case "WEEK" -> "to_char(date_trunc('week', dd.full_date), 'YYYY-MM-DD')";
            case "MONTH" -> "to_char(date_trunc('month', dd.full_date), 'YYYY-MM')";
            default -> "to_char(dd.full_date, 'YYYY-MM-DD')";
        };
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT %s AS bucket,
                       COALESCE(SUM(fs.gross_sales_amount),0) AS gross_sales,
                       COALESCE(SUM(fs.net_sales_amount),0) AS net_sales,
                       COALESCE(SUM(fs.units_sold),0) AS units,
                       COALESCE(SUM(fs.orders_count),0) AS orders
                FROM ama.fact_sales_daily fs
                JOIN ama.product p ON p.product_id = fs.product_id
                JOIN ama.dim_date dd ON dd.date_key = fs.date_key
                """.formatted(bucketExpr));
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fs", "p");
        sql.append(" GROUP BY bucket ORDER BY bucket ");
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new SalesTimeSeriesPoint(
                rs.getString("bucket"), rs.getDouble("gross_sales"), rs.getDouble("net_sales"), rs.getLong("units"), rs.getLong("orders")
        ));
    }

    public List<ProductSalesRow> findSalesByProduct(AnalyticsPagedRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.product_id, p.sku, p.asin, p.product_name,
                       COALESCE(SUM(fs.units_sold),0) AS units_sold,
                       COALESCE(SUM(fs.orders_count),0) AS orders,
                       COALESCE(SUM(fs.net_sales_amount),0) AS net_sales,
                       COALESCE(AVG(fs.avg_selling_price),0) AS avg_selling_price
                FROM ama.fact_sales_daily fs
                JOIN ama.product p ON p.product_id = fs.product_id
                JOIN ama.dim_date dd ON dd.date_key = fs.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fs", "p");
        sql.append(" GROUP BY p.product_id, p.sku, p.asin, p.product_name ");
        sql.append(" ORDER BY net_sales DESC ");
        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset ");
            params.put("limit", request.getPagination().sizeOrDefault());
            params.put("offset", request.getPagination().pageOrDefault() * request.getPagination().sizeOrDefault());
        }
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new ProductSalesRow(
                rs.getLong("product_id"), rs.getString("sku"), rs.getString("asin"), rs.getString("product_name"),
                rs.getLong("units_sold"), rs.getLong("orders"), rs.getDouble("net_sales"), rs.getDouble("avg_selling_price")
        ));
    }
}
