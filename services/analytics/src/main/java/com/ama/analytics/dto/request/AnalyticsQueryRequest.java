package com.ama.analytics.dto.request;

import com.ama.common.dto.DateRangeDto;
import com.ama.common.dto.PageRequestDto;
import com.ama.common.dto.SortRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AnalyticsQueryRequest {
    @NotNull
    private Long tenantId;
    @NotEmpty
    private List<String> metrics;
    private List<String> dimensions;
    @Valid
    @NotNull
    private DateRangeDto dateRange;
    @Valid
    private QueryFilters filters;
    @Valid
    private PageRequestDto pagination;
    @Valid
    private SortRequestDto sort;
    private String granularity;

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public List<String> getMetrics() { return metrics; }
    public void setMetrics(List<String> metrics) { this.metrics = metrics; }
    public List<String> getDimensions() { return dimensions; }
    public void setDimensions(List<String> dimensions) { this.dimensions = dimensions; }
    public DateRangeDto getDateRange() { return dateRange; }
    public void setDateRange(DateRangeDto dateRange) { this.dateRange = dateRange; }
    public QueryFilters getFilters() { return filters; }
    public void setFilters(QueryFilters filters) { this.filters = filters; }
    public PageRequestDto getPagination() { return pagination; }
    public void setPagination(PageRequestDto pagination) { this.pagination = pagination; }
    public SortRequestDto getSort() { return sort; }
    public void setSort(SortRequestDto sort) { this.sort = sort; }
    public String getGranularity() { return granularity; }
    public void setGranularity(String granularity) { this.granularity = granularity; }

    public static class QueryFilters {
        private List<Long> accountIds;
        private List<String> marketplaceCodes;
        private List<Long> brandIds;
        private List<Long> categoryIds;
        private List<Long> productIds;

        public List<Long> getAccountIds() { return accountIds; }
        public void setAccountIds(List<Long> accountIds) { this.accountIds = accountIds; }
        public List<String> getMarketplaceCodes() { return marketplaceCodes; }
        public void setMarketplaceCodes(List<String> marketplaceCodes) { this.marketplaceCodes = marketplaceCodes; }
        public List<Long> getBrandIds() { return brandIds; }
        public void setBrandIds(List<Long> brandIds) { this.brandIds = brandIds; }
        public List<Long> getCategoryIds() { return categoryIds; }
        public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
        public List<Long> getProductIds() { return productIds; }
        public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
    }
}
