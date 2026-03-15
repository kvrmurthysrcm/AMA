package com.ama.analytics.dto.response;

public record AdsOverviewResponse(
        long impressions,
        long clicks,
        double ctr,
        double spend,
        double cpc,
        double attributedSales,
        double acos,
        double roas,
        long conversions
) {}
