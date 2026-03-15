package com.ama.analytics.dto.response;

public record DashboardTrendPoint(
        String bucket,
        double sales,
        long orders,
        long units,
        double adSpend,
        double profit
) {}
