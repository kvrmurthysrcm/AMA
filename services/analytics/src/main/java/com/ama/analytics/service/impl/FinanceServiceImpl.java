package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.ProfitabilityResponse;
import com.ama.analytics.repo.FinanceAnalyticsRepository;
import com.ama.analytics.service.FinanceService;
import org.springframework.stereotype.Service;

@Service
public class FinanceServiceImpl implements FinanceService {
    private final FinanceAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public FinanceServiceImpl(FinanceAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public ProfitabilityResponse getProfitability(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.fetchProfitability(request);
    }
}
