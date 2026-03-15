package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.InsightCard;
import com.ama.analytics.repo.InsightsRepository;
import com.ama.analytics.service.InsightsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsightsServiceImpl implements InsightsService {
    private final InsightsRepository repository;
    private final AnalyticsValidationService validationService;

    public InsightsServiceImpl(InsightsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public List<InsightCard> getSummary(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.deriveInsights(request);
    }
}
