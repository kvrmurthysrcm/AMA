package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.service.SalesService;
import com.ama.common.api.ApiMeta;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/sales")
public class SalesController extends BaseAnalyticsController {
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/timeseries")
    public ApiResponse<?> timeseries(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Sales timeseries fetched successfully", salesService.getTimeseries(request));
    }

    @PostMapping("/by-product")
    public ApiResponse<?> byProduct(@Valid @RequestBody AnalyticsPagedRequest request) {
        var items = salesService.getSalesByProduct(request);
        var meta = request.getPagination() == null ? ApiMeta.empty() : new ApiMeta(request.getPagination().pageOrDefault(), request.getPagination().sizeOrDefault(), (long) items.size(), 1);
        return ok("Sales by product fetched successfully", items, meta);
    }
}
