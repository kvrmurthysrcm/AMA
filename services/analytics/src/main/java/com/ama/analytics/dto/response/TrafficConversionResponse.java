package com.ama.analytics.dto.response;

public record TrafficConversionResponse(
        long impressions,
        long clicks,
        double ctr,
        long sessions,
        long pageViews,
        double conversionRate
) {}
