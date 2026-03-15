package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.service.InsightsService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/insights")
public class InsightsController extends BaseAnalyticsController {
    private final InsightsService insightsService;

    public InsightsController(InsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @PostMapping("/summary")
    public ApiResponse<?> summary(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Insights summary fetched successfully", insightsService.getSummary(request));
    }
}
