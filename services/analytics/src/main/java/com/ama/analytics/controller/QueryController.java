package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsQueryRequest;
import com.ama.analytics.service.FlexibleQueryService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1")
public class QueryController extends BaseAnalyticsController {
    private final FlexibleQueryService flexibleQueryService;

    public QueryController(FlexibleQueryService flexibleQueryService) {
        this.flexibleQueryService = flexibleQueryService;
    }

    @PostMapping("/query")
    public ApiResponse<?> query(@Valid @RequestBody AnalyticsQueryRequest request) {
        return ok("Flexible analytics query executed successfully", flexibleQueryService.execute(request));
    }
}
