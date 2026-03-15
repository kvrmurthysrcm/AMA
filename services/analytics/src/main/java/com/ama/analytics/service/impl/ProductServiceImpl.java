package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.ProductPerformanceRow;
import com.ama.analytics.repo.ProductAnalyticsRepository;
import com.ama.analytics.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public ProductServiceImpl(ProductAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public List<ProductPerformanceRow> getPerformance(AnalyticsPagedRequest request) {
        validationService.validatePagedRequest(request);
        return repository.fetchPerformance(request);
    }
}
