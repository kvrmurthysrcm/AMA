package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.DashboardSummaryResponse;
import com.ama.analytics.dto.response.DashboardTrendPoint;
import com.ama.analytics.repo.DashboardAnalyticsRepository;
import com.ama.analytics.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final DashboardAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public DashboardServiceImpl(DashboardAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public DashboardSummaryResponse getSummary(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.fetchSummary(request);
    }

    @Override
    public List<DashboardTrendPoint> getTrends(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.fetchTrends(request);
    }
}
