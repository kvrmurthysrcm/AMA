package com.ama.analytics.dto.response;

public record InsightCard(
        String type,
        String severity,
        String title,
        String description,
        Double metricImpact,
        String recommendation
) {}
