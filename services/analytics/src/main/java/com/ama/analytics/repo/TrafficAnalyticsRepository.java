package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.TrafficConversionResponse;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TrafficAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TrafficAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TrafficConversionResponse fetchConversion(AnalyticsFilterRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT COALESCE(SUM(fa.impressions_count),0) AS impressions,
                       COALESCE(SUM(fa.clicks_count),0) AS clicks,
                       CASE WHEN COALESCE(SUM(fa.impressions_count),0) = 0 THEN 0 ELSE (COALESCE(SUM(fa.clicks_count),0)::numeric / NULLIF(SUM(fa.impressions_count),0)) * 100 END AS ctr,
                       COALESCE(SUM(ft.sessions_count),0) AS sessions,
                       COALESCE(SUM(ft.page_views_count),0) AS page_views,
                       CASE WHEN COALESCE(SUM(ft.sessions_count),0) = 0 THEN 0 ELSE (COALESCE(SUM(fs.units_sold),0)::numeric / NULLIF(SUM(ft.sessions_count),0)) * 100 END AS conversion_rate
                FROM ama.fact_traffic_daily ft
                LEFT JOIN ama.fact_advertising_daily fa ON fa.tenant_id = ft.tenant_id AND fa.account_id = ft.account_id AND fa.product_id = ft.product_id AND fa.date_key = ft.date_key
                LEFT JOIN ama.fact_sales_daily fs ON fs.tenant_id = ft.tenant_id AND fs.account_id = ft.account_id AND fs.product_id = ft.product_id AND fs.date_key = ft.date_key
                JOIN ama.product p ON p.product_id = ft.product_id
                JOIN ama.dim_date dd ON dd.date_key = ft.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "ft", "p");
        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> new TrafficConversionResponse(
                rs.getLong("impressions"), rs.getLong("clicks"), rs.getDouble("ctr"), rs.getLong("sessions"), rs.getLong("page_views"), rs.getDouble("conversion_rate")
        ));
    }
}
