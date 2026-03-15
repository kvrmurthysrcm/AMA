package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.service.TrafficService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/traffic")
public class TrafficController extends BaseAnalyticsController {
    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @PostMapping("/conversion")
    public ApiResponse<?> conversion(@Valid @RequestBody AnalyticsFilterRequest request) {
        return ok("Traffic conversion fetched successfully", trafficService.getConversion(request));
    }
}
