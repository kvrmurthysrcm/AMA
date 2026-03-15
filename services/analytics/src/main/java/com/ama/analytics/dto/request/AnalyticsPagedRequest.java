package com.ama.analytics.dto.request;

import com.ama.common.dto.PageRequestDto;
import com.ama.common.dto.SortRequestDto;
import jakarta.validation.Valid;

public class AnalyticsPagedRequest extends AnalyticsFilterRequest {
    @Valid
    private PageRequestDto pagination;
    @Valid
    private SortRequestDto sort;

    public PageRequestDto getPagination() { return pagination; }
    public void setPagination(PageRequestDto pagination) { this.pagination = pagination; }
    public SortRequestDto getSort() { return sort; }
    public void setSort(SortRequestDto sort) { this.sort = sort; }
}
