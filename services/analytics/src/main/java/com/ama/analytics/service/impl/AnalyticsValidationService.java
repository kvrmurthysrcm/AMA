package com.ama.analytics.service.impl;

import com.ama.analytics.constants.AnalyticsConstants;
import com.ama.analytics.dto.request.AnalyticsFilterRequest;
import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.request.AnalyticsQueryRequest;
import com.ama.common.exception.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AnalyticsValidationService {

    public void validateDateRange(AnalyticsFilterRequest request) {
        if (request.getDateRange().startDate().isAfter(request.getDateRange().endDate())) {
            throw new BadRequestException("startDate cannot be after endDate");
        }
        if (request.getGranularity() != null && !AnalyticsConstants.ALLOWED_GRANULARITIES.contains(request.getGranularity().toUpperCase())) {
            throw new BadRequestException("Unsupported granularity: " + request.getGranularity());
        }
    }

    public void validatePagedRequest(AnalyticsPagedRequest request) {
        validateDateRange(request);
        if (request.getPagination() != null && request.getPagination().sizeOrDefault() > 500) {
            throw new BadRequestException("Page size cannot exceed 500");
        }
    }

    public void validateQueryRequest(AnalyticsQueryRequest request) {
        if (request.getDateRange().startDate().isAfter(request.getDateRange().endDate())) {
            throw new BadRequestException("startDate cannot be after endDate");
        }
        request.getMetrics().forEach(metric -> {
            if (!AnalyticsConstants.ALLOWED_QUERY_METRICS.contains(metric)) {
                throw new BadRequestException("Unsupported metric: " + metric);
            }
        });
        if (!CollectionUtils.isEmpty(request.getDimensions())) {
            request.getDimensions().forEach(dimension -> {
                if (!AnalyticsConstants.ALLOWED_QUERY_DIMENSIONS.contains(dimension)) {
                    throw new BadRequestException("Unsupported dimension: " + dimension);
                }
            });
        }
    }
}
