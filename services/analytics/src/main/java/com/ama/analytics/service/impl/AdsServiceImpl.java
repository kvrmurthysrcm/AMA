package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.AdsOverviewResponse;
import com.ama.analytics.dto.response.ProductAdsRow;
import com.ama.analytics.repo.AdsAnalyticsRepository;
import com.ama.analytics.service.AdsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdsServiceImpl implements AdsService {
    private final AdsAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public AdsServiceImpl(AdsAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public AdsOverviewResponse getOverview(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.fetchOverview(request);
    }

    @Override
    public List<ProductAdsRow> getByProduct(AnalyticsPagedRequest request) {
        validationService.validatePagedRequest(request);
        return repository.fetchByProduct(request);
    }
}
