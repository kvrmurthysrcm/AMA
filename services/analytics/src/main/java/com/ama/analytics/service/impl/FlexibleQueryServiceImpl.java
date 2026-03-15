package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsQueryRequest;
import com.ama.analytics.dto.response.AnalyticsQueryResponse;
import com.ama.analytics.repo.FlexibleQueryRepository;
import com.ama.analytics.service.FlexibleQueryService;
import org.springframework.stereotype.Service;

@Service
public class FlexibleQueryServiceImpl implements FlexibleQueryService {
    private final FlexibleQueryRepository repository;
    private final AnalyticsValidationService validationService;

    public FlexibleQueryServiceImpl(FlexibleQueryRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public AnalyticsQueryResponse execute(AnalyticsQueryRequest request) {
        validationService.validateQueryRequest(request);
        return repository.execute(request);
    }
}
