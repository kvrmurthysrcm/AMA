package com.ama.analytics.repo;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.AdsOverviewResponse;
import com.ama.analytics.dto.response.ProductAdsRow;
import com.ama.analytics.query.FilterSqlBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdsAnalyticsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AdsAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AdsOverviewResponse fetchOverview(AnalyticsFilterRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT COALESCE(SUM(fa.impressions_count),0) AS impressions,
                       COALESCE(SUM(fa.clicks_count),0) AS clicks,
                       CASE WHEN COALESCE(SUM(fa.impressions_count),0) = 0 THEN 0 ELSE (COALESCE(SUM(fa.clicks_count),0)::numeric / NULLIF(SUM(fa.impressions_count),0)) * 100 END AS ctr,
                       COALESCE(SUM(fa.spend_amount),0) AS spend,
                       CASE WHEN COALESCE(SUM(fa.clicks_count),0) = 0 THEN 0 ELSE COALESCE(SUM(fa.spend_amount),0) / NULLIF(SUM(fa.clicks_count),0) END AS cpc,
                       COALESCE(SUM(fa.ad_sales_amount),0) AS attributed_sales,
                       CASE WHEN COALESCE(SUM(fa.ad_sales_amount),0) = 0 THEN 0 ELSE (COALESCE(SUM(fa.spend_amount),0) / NULLIF(SUM(fa.ad_sales_amount),0)) * 100 END AS acos,
                       CASE WHEN COALESCE(SUM(fa.spend_amount),0) = 0 THEN 0 ELSE COALESCE(SUM(fa.ad_sales_amount),0) / NULLIF(SUM(fa.spend_amount),0) END AS roas,
                       COALESCE(SUM(fa.conversions_count),0) AS conversions
                FROM ama.fact_advertising_daily fa
                JOIN ama.product p ON p.product_id = fa.product_id
                JOIN ama.dim_date dd ON dd.date_key = fa.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fa", "p");
        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> new AdsOverviewResponse(
                rs.getLong("impressions"), rs.getLong("clicks"), rs.getDouble("ctr"), rs.getDouble("spend"), rs.getDouble("cpc"), rs.getDouble("attributed_sales"), rs.getDouble("acos"), rs.getDouble("roas"), rs.getLong("conversions")
        ));
    }

    public List<ProductAdsRow> fetchByProduct(AnalyticsPagedRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.product_id, p.sku, p.product_name,
                       COALESCE(SUM(fa.spend_amount),0) AS spend,
                       COALESCE(SUM(fa.clicks_count),0) AS clicks,
                       CASE WHEN COALESCE(SUM(fa.clicks_count),0) = 0 THEN 0 ELSE COALESCE(SUM(fa.spend_amount),0) / NULLIF(SUM(fa.clicks_count),0) END AS cpc,
                       COALESCE(SUM(fa.ad_sales_amount),0) AS attributed_sales,
                       CASE WHEN COALESCE(SUM(fa.ad_sales_amount),0) = 0 THEN 0 ELSE (COALESCE(SUM(fa.spend_amount),0) / NULLIF(SUM(fa.ad_sales_amount),0)) * 100 END AS acos,
                       CASE WHEN COALESCE(SUM(fa.spend_amount),0) = 0 THEN 0 ELSE COALESCE(SUM(fa.ad_sales_amount),0) / NULLIF(SUM(fa.spend_amount),0) END AS roas
                FROM ama.fact_advertising_daily fa
                JOIN ama.product p ON p.product_id = fa.product_id
                JOIN ama.dim_date dd ON dd.date_key = fa.date_key
                """);
        FilterSqlBuilder.appendCommonFilters(sql, params, request, "fa", "p");
        sql.append(" GROUP BY p.product_id, p.sku, p.product_name ORDER BY spend DESC ");
        if (request.getPagination() != null) {
            sql.append(" LIMIT :limit OFFSET :offset ");
            params.put("limit", request.getPagination().sizeOrDefault());
            params.put("offset", request.getPagination().pageOrDefault() * request.getPagination().sizeOrDefault());
        }
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> new ProductAdsRow(
                rs.getLong("product_id"), rs.getString("sku"), rs.getString("product_name"), rs.getDouble("spend"), rs.getLong("clicks"), rs.getDouble("cpc"), rs.getDouble("attributed_sales"), rs.getDouble("acos"), rs.getDouble("roas")
        ));
    }
}
