package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.service.InventoryService;
import com.ama.common.api.ApiMeta;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/inventory")
public class InventoryController extends BaseAnalyticsController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/stockout-risk")
    public ApiResponse<?> stockoutRisk(@Valid @RequestBody AnalyticsPagedRequest request) {
        var items = inventoryService.getStockoutRisk(request);
        var meta = request.getPagination() == null ? ApiMeta.empty() : new ApiMeta(request.getPagination().pageOrDefault(), request.getPagination().sizeOrDefault(), (long) items.size(), 1);
        return ok("Inventory stockout risk fetched successfully", items, meta);
    }
}
