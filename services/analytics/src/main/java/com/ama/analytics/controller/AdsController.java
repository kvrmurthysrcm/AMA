package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.service.AdsService;
import com.ama.common.api.ApiMeta;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/ads")
public class AdsController extends BaseAnalyticsController {
    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    @PostMapping("/overview")
    public ApiResponse<?> overview(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Ads overview fetched successfully", adsService.getOverview(request));
    }

    @PostMapping("/by-product")
    public ApiResponse<?> byProduct(@Valid @RequestBody AnalyticsPagedRequest request) {
        var items = adsService.getByProduct(request);
        var meta = request.getPagination() == null ? ApiMeta.empty() : new ApiMeta(request.getPagination().pageOrDefault(), request.getPagination().sizeOrDefault(), (long) items.size(), 1);
        return ok("Ads by product fetched successfully", items, meta);
    }
}
