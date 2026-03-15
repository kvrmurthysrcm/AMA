package com.ama.analytics.repo;

import com.ama.analytics.dto.request.MetadataFilterRequest;
import com.ama.analytics.dto.request.TenantRequest;
import com.ama.analytics.dto.response.AccountMetadataItem;
import com.ama.analytics.dto.response.BrandMetadataItem;
import com.ama.analytics.dto.response.CategoryMetadataItem;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MetadataRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MetadataRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AccountMetadataItem> findAccounts(TenantRequest request) {
        return jdbcTemplate.query("""
                SELECT account_id, marketplace_code, marketplace_name, seller_account_ref, connection_status
                FROM ama.tenant_marketplace_account
                WHERE tenant_id = :tenantId
                ORDER BY marketplace_code, account_id
                """, Map.of("tenantId", request.getTenantId()), (rs, rowNum) -> new AccountMetadataItem(
                rs.getLong("account_id"), rs.getString("marketplace_code"), rs.getString("marketplace_name"), rs.getString("seller_account_ref"), rs.getString("connection_status")
        ));
    }

    public List<BrandMetadataItem> findBrands(MetadataFilterRequest request) {
        return jdbcTemplate.query("""
                SELECT brand_id, brand_name
                FROM ama.brand
                WHERE tenant_id = :tenantId
                ORDER BY brand_name
                """, Map.of("tenantId", request.getTenantId()), (rs, rowNum) -> new BrandMetadataItem(rs.getLong("brand_id"), rs.getString("brand_name")));
    }

    public List<CategoryMetadataItem> findCategories(MetadataFilterRequest request) {
        return jdbcTemplate.query("""
                SELECT category_id, category_name
                FROM ama.category
                WHERE tenant_id = :tenantId
                ORDER BY category_name
                """, Map.of("tenantId", request.getTenantId()), (rs, rowNum) -> new CategoryMetadataItem(rs.getLong("category_id"), rs.getString("category_name")));
    }
}
