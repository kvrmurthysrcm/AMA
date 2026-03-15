package com.ama.analytics.dto.request;

import jakarta.validation.constraints.NotNull;

public class TenantRequest {
    @NotNull
    private Long tenantId;

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
}
