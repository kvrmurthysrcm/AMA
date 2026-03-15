package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.DashboardSummaryResponse;
import com.ama.analytics.dto.response.DashboardTrendPoint;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DashboardAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DashboardSummaryResponse fetchSummary(AnalyticsFilterRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    COALESCE(SUM(fs.net_sales_amount), 0) AS total_sales,
                    COALESCE(SUM(fs.orders_count), 0) AS total_orders,
                    COALESCE(SUM(fs.units_sold), 0) AS total_units,
                    COALESCE(SUM(fa.spend_amount), 0) AS ad_spend,
                    CASE WHEN COALESCE(SUM(fa.spend_amount), 0) = 0 THEN 0 ELSE COALESCE(SUM(fa.ad_sales_amount), 0) / NULLIF(SUM(fa.spend_amount), 0) END AS roas,
                    CASE WHEN COALESCE(SUM(fa.ad_sales_amount), 0) = 0 THEN 0 ELSE (COALESCE(SUM(fa.spend_amount), 0) / NULLIF(SUM(fa.ad_sales_amount), 0)) * 100 END AS acos,
                    COALESCE(SUM(ff.estimated_profit_amount), 0) AS profit,
                    CASE WHEN COALESCE(SUM(ft.sessions_count), 0) = 0 THEN 0 ELSE (COALESCE(SUM(fs.units_sold), 0)::numeric / NULLIF(SUM(ft.sessions_count), 0)) * 100 END AS conversion_rate
                FROM ama.fact_sales_daily fs
                LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = fs.tenant_id AND fa.account_id = fs.account_id AND fa.product_id = fs.product_id AND fa.date_key = fs.date_key
                LEFT JOIN ama.fact_finance_daily ff ON ff.tenant_id = fs.tenant_id AND ff.account_id = fs.account_id AND ff.product_id = fs.product_id AND ff.date_key = fs.date_key
                LEFT JOIN ama.fact_traffic_daily ft ON ft.tenant_id = fs.tenant_id AND ft.account_id = fs.account_id AND ft.product_id = fs.product_id AND ft.date_key = fs.date_key
                JOIN ama.product p ON p.product_id = fs.product_id
                JOIN ama.dim_date dd ON dd.date_key = fs.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fs", "p");
        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) ->
                new DashboardSummaryResponse(
                        new DashboardSummaryResponse.SummaryMetrics(
                                rs.getDouble("total_sales"),
                                rs.getLong("total_orders"),
                                rs.getLong("total_units"),
                                rs.getDouble("ad_spend"),
                                rs.getDouble("roas"),
                                rs.getDouble("acos"),
                                rs.getDouble("profit"),
                                rs.getDouble("conversion_rate")
                        ),
                        new DashboardSummaryResponse.PeriodDelta(0, 0, 0)
                ));
    }

    public List<DashboardTrendPoint> fetchTrends(AnalyticsFilterRequest request) {
        String bucketExpr = switch (request.getGranularity() == null ? "DAY" : request.getGranularity().toUpperCase()) {
            case "WEEK" -> "to_char(date_trunc('week', dd.full_date), 'YYYY-MM-DD')";
            case "MONTH" -> "to_char(date_trunc('month', dd.full_date), 'YYYY-MM')";
            default -> "to_char(dd.full_date, 'YYYY-MM-DD')";
        };
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT %s AS bucket,
                       COALESCE(SUM(fs.net_sales_amount),0) AS sales,
                       COALESCE(SUM(fs.orders_count),0) AS orders,
                       COALESCE(SUM(fs.units_sold),0) AS units,
                       COALESCE(SUM(fa.spend_amount),0) AS ad_spend,
                       COALESCE(SUM(ff.estimated_profit_amount),0) AS profit
                FROM ama.fact_sales_daily fs
                LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = fs.tenant_id AND fa.account_id = fs.account_id AND fa.product_id = fs.product_id AND fa.date_key = fs.date_key
                LEFT JOIN ama.fact_finance_daily ff ON ff.tenant_id = fs.tenant_id AND ff.account_id = fs.account_id AND ff.product_id = fs.product_id AND ff.date_key = fs.date_key
                JOIN ama.product p ON p.product_id = fs.product_id
                JOIN ama.dim_date dd ON dd.date_key = fs.date_key
                """.formatted(bucketExpr));
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fs", "p");
        sql.append(" GROUP BY bucket ORDER BY bucket ");
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new DashboardTrendPoint(
                rs.getString("bucket"),
                rs.getDouble("sales"),
                rs.getLong("orders"),
                rs.getLong("units"),
                rs.getDouble("ad_spend"),
                rs.getDouble("profit")
        ));
    }
}
