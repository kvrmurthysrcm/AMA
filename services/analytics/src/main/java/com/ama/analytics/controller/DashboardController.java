package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.service.DashboardService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/dashboard")
public class DashboardController extends BaseAnalyticsController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PostMapping("/summary")
    public ApiResponse<?> summary(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Dashboard summary fetched successfully", dashboardService.getSummary(request));
    }

    @PostMapping("/trends")
    public ApiResponse<?> trends(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Dashboard trends fetched successfully", dashboardService.getTrends(request));
    }
}
