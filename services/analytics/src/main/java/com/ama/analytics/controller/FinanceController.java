package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.service.FinanceService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/finance")
public class FinanceController extends BaseAnalyticsController {
    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping("/profitability")
    public ApiResponse<?> profitability(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Finance profitability fetched successfully", financeService.getProfitability(request));
    }
}
