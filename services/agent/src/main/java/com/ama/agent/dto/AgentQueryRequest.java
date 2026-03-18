package com.ama.agent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AgentQueryRequest {

    @NotBlank
    private String question;

    @NotNull
    private Long tenantId;

    private List<Long> accountIds;
    private List<String> marketplaceCodes;

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public List<Long> getAccountIds() { return accountIds; }
    public void setAccountIds(List<Long> accountIds) { this.accountIds = accountIds; }

    public List<String> getMarketplaceCodes() { return marketplaceCodes; }
    public void setMarketplaceCodes(List<String> marketplaceCodes) { this.marketplaceCodes = marketplaceCodes; }
}
