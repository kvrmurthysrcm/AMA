package com.ama.analytics.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MetadataFilterRequest {
    @NotNull
    private Long tenantId;
    private List<Long> accountIds;
    private List<Long> brandIds;

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public List<Long> getAccountIds() { return accountIds; }
    public void setAccountIds(List<Long> accountIds) { this.accountIds = accountIds; }
    public List<Long> getBrandIds() { return brandIds; }
    public void setBrandIds(List<Long> brandIds) { this.brandIds = brandIds; }
}
