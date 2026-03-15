package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.response.TrafficConversionResponse;
import com.ama.analytics.repo.TrafficAnalyticsRepository;
import com.ama.analytics.service.TrafficService;
import org.springframework.stereotype.Service;

@Service
public class TrafficServiceImpl implements TrafficService {
    private final TrafficAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public TrafficServiceImpl(TrafficAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public TrafficConversionResponse getConversion(AnalyticsFilterRequest request) {
        validationService.validateDateRange(request);
        return repository.fetchConversion(request);
    }
}
