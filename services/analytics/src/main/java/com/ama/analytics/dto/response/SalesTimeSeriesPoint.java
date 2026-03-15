package com.ama.analytics.dto.response;

public record SalesTimeSeriesPoint(
        String bucket,
        double grossSales,
        double netSales,
        long units,
        long orders
) {}
