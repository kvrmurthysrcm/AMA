package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.ProductSalesRow;
import com.ama.analytics.dto.response.SalesTimeSeriesPoint;
import com.ama.analytics.repo.SalesAnalyticsRepository;
import com.ama.analytics.service.SalesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    private final SalesAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public SalesServiceImpl(SalesAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public List<SalesTimeSeriesPoint> getTimeseries(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.findSalesTimeseries(request);
    }

    @Override
    public List<ProductSalesRow> getSalesByProduct(AnalyticsPagedRequest request) {
        validationService.validatePagedRequest(request);
        return repository.findSalesByProduct(request);
    }
}
