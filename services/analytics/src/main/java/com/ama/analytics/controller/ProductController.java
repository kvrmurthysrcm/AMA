package com.ama.analytics.controller;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.service.ProductService;
import com.ama.common.api.ApiMeta;
import com.ama.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics/v1/products")
public class ProductController extends BaseAnalyticsController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/performance")
    public ApiResponse<?> performance(@Valid @RequestBody AnalyticsPagedRequest request) {
        var items = productService.getPerformance(request);
        var meta = request.getPagination() == null ? ApiMeta.empty() : new ApiMeta(request.getPagination().pageOrDefault(), request.getPagination().sizeOrDefault(), (long) items.size(), 1);
        return ok("Product performance fetched successfully", items, meta);
    }
}
