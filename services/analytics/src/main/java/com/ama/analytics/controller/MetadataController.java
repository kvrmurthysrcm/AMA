package com.ama.analytics.controller;

import com.ama.analytics.dto.request.MetadataFilterRequest;
import com.ama.analytics.dto.request.TenantRequest;
import com.ama.analytics.service.MetadataService;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/metadata")
public class MetadataController extends BaseAnalyticsController {
    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @PostMapping("/accounts")
    public ApiResponse<?> accounts(@Valid @RequestBody TenantRequest request) {
        return ok("Account metadata fetched successfully", metadataService.getAccounts(request));
    }

    @PostMapping("/brands")
    public ApiResponse<?> brands(@Valid @RequestBody MetadataFilterRequest request) {
        return ok("Brand metadata fetched successfully", metadataService.getBrands(request));
    }

    @PostMapping("/categories")
    public ApiResponse<?> categories(@Valid @RequestBody MetadataFilterRequest request) {
        return ok("Category metadata fetched successfully", metadataService.getCategories(request));
    }
}
