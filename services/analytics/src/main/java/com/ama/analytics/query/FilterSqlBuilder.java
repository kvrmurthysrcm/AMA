package com.ama.analytics.query;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsQueryRequest;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public final class FilterSqlBuilder {
    private FilterSqlBuilder() {
    }

    public static String appendCommonFilters(StringBuilder sql, Map<String, Object> params, AnalyticsFilterRequest request,
                                             String salesAlias, String productAlias) {
        sql.append(" WHERE ").append(salesAlias).append(".tenant_id = :tenantId ");
        params.put("tenantId", request.getTenantId());
        sql.append(" AND dd.full_date BETWEEN :startDate AND :endDate ");
        params.put("startDate", request.getDateRange().startDate());
        params.put("endDate", request.getDateRange().endDate());
        if (!CollectionUtils.isEmpty(request.getAccountIds())) {
            sql.append(" AND ").append(salesAlias).append(".account_id IN (:accountIds) ");
            params.put("accountIds", request.getAccountIds());
        }
        if (!CollectionUtils.isEmpty(request.getMarketplaceCodes())) {
            sql.append(" AND ").append(salesAlias).append(".marketplace_code IN (:marketplaceCodes) ");
            params.put("marketplaceCodes", request.getMarketplaceCodes());
        }
        if (!CollectionUtils.isEmpty(request.getBrandIds())) {
            sql.append(" AND ").append(productAlias).append(".brand_id IN (:brandIds) ");
            params.put("brandIds", request.getBrandIds());
        }
        if (!CollectionUtils.isEmpty(request.getCategoryIds())) {
            sql.append(" AND ").append(productAlias).append(".category_id IN (:categoryIds) ");
            params.put("categoryIds", request.getCategoryIds());
        }
        if (!CollectionUtils.isEmpty(request.getProductIds())) {
            sql.append(" AND ").append(productAlias).append(".product_id IN (:productIds) ");
            params.put("productIds", request.getProductIds());
        }
        return sql.toString();
    }

    public static String appendQueryFilters(StringBuilder sql, Map<String, Object> params, AnalyticsQueryRequest request) {
        sql.append(" WHERE fs.tenant_id = :tenantId ");
        params.put("tenantId", request.getTenantId());
        sql.append(" AND dd.full_date BETWEEN :startDate AND :endDate ");
        params.put("startDate", request.getDateRange().startDate());
        params.put("endDate", request.getDateRange().endDate());
        if (request.getFilters() != null) {
            if (!CollectionUtils.isEmpty(request.getFilters().getAccountIds())) {
                sql.append(" AND fs.account_id IN (:accountIds) ");
                params.put("accountIds", request.getFilters().getAccountIds());
            }
            if (!CollectionUtils.isEmpty(request.getFilters().getMarketplaceCodes())) {
                sql.append(" AND fs.marketplace_code IN (:marketplaceCodes) ");
                params.put("marketplaceCodes", request.getFilters().getMarketplaceCodes());
            }
            if (!CollectionUtils.isEmpty(request.getFilters().getBrandIds())) {
                sql.append(" AND p.brand_id IN (:brandIds) ");
                params.put("brandIds", request.getFilters().getBrandIds());
            }
            if (!CollectionUtils.isEmpty(request.getFilters().getCategoryIds())) {
                sql.append(" AND p.category_id IN (:categoryIds) ");
                params.put("categoryIds", request.getFilters().getCategoryIds());
            }
            if (!CollectionUtils.isEmpty(request.getFilters().getProductIds())) {
                sql.append(" AND p.product_id IN (:productIds) ");
                params.put("productIds", request.getFilters().getProductIds());
            }
        }
        return sql.toString();
    }
}
