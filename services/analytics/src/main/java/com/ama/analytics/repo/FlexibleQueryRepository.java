package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsQueryRequest;
import com.ama.analytics.dto.response.AnalyticsQueryResponse;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FlexibleQueryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final Map<String, String> METRIC_SQL = Map.of(
            "grossSales", "COALESCE(SUM(fs.gross_sales_amount),0) AS grossSales",
            "netSales", "COALESCE(SUM(fs.net_sales_amount),0) AS netSales",
            "unitsSold", "COALESCE(SUM(fs.units_sold),0) AS unitsSold",
            "ordersCount", "COALESCE(SUM(fs.orders_count),0) AS ordersCount",
            "adSpend", "COALESCE(SUM(fa.spend_amount),0) AS adSpend",
            "adSales", "COALESCE(SUM(fa.ad_sales_amount),0) AS adSales",
            "roas", "CASE WHEN COALESCE(SUM(fa.spend_amount),0)=0 THEN 0 ELSE COALESCE(SUM(fa.ad_sales_amount),0)/NULLIF(SUM(fa.spend_amount),0) END AS roas",
            "acos", "CASE WHEN COALESCE(SUM(fa.ad_sales_amount),0)=0 THEN 0 ELSE (COALESCE(SUM(fa.spend_amount),0)/NULLIF(SUM(fa.ad_sales_amount),0))*100 END AS acos",
            "sessions", "COALESCE(SUM(ft.sessions_count),0) AS sessions",
            "pageViews", "COALESCE(SUM(ft.page_views_count),0) AS pageViews"
    );

    private static final Map<String, String> DIMENSION_SQL = Map.of(
            "date", "to_char(dd.full_date, 'YYYY-MM-DD') AS date",
            "brandName", "b.brand_name AS brandName",
            "categoryName", "c.category_name AS categoryName",
            "productName", "p.product_name AS productName",
            "marketplaceCode", "fs.marketplace_code AS marketplaceCode",
            "accountId", "CAST(fs.account_id AS varchar) AS accountId"
    );

    public FlexibleQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AnalyticsQueryResponse execute(AnalyticsQueryRequest request) {
        List<String> selectParts = new ArrayList<>();
        List<String> groupBy = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        if (request.getDimensions() != null) {
            for (String dimension : request.getDimensions()) {
                selectParts.add(DIMENSION_SQL.get(dimension));
                groupBy.add(switch (dimension) {
                    case "date" -> "dd.full_date";
                    case "brandName" -> "b.brand_name";
                    case "categoryName" -> "c.category_name";
                    case "productName" -> "p.product_name";
                    case "marketplaceCode" -> "fs.marketplace_code";
                    case "accountId" -> "fs.account_id";
                    default -> throw new IllegalArgumentException("Unsupported dimension " + dimension);
                });
                columns.add(dimension);
            }
        }
        for (String metric : request.getMetrics()) {
            selectParts.add(METRIC_SQL.get(metric));
            columns.add(metric);
        }

        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT ").append(String.join(", ", selectParts)).append(" " )
                .append(" FROM ama.fact_sales_daily fs ")
                .append(" LEFT JOIN ama.fact_traffic_daily ft ON ft.tenant_id = fs.tenant_id AND ft.account_id = fs.account_id AND ft.product_id = fs.product_id AND ft.date_key = fs.date_key ")
                .append(" LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = fs.tenant_id AND fa.account_id = fs.account_id AND fa.product_id = fs.product_id AND fa.date_key = fs.date_key ")
                .append(" JOIN ama.product p ON p.product_id = fs.product_id ")
                .append(" LEFT JOIN ama.brand b ON b.brand_id = p.brand_id ")
                .append(" LEFT JOIN ama.category c ON c.category_id = p.category_id ")
                .append(" JOIN ama.dim_date dd ON dd.date_key = fs.date_key ");
        FilterSqlBuilder.appendQueryFilters(sql, params, request);
        if (!groupBy.isEmpty()) {
            sql.append(" GROUP BY ").append(String.join(", ", groupBy));
        }
        if (request.getSort() != null && request.getSort().field() != null && columns.contains(request.getSort().field())) {
            sql.append(" ORDER BY ").append(request.getSort().field()).append(" ").append(request.getSort().directionOrDefault());
        }
        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset");
            params.put("limit", request.getPagination().sizeOrDefault());
            params.put("offset", request.getPagination().pageOrDefault() * request.getPagination().sizeOrDefault());
        }
        List<Map<String, Object>> rows = jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            Map<String, Object> row = new LinkedHashMap<>();
            for (String column : columns) {
                row.put(column, rs.getObject(column));
            }
            return row;
        });
        return new AnalyticsQueryResponse(columns, rows, Map.of());
    }
}
