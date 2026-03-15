package com.ama.analytics.dto.response;

public record ProfitabilityResponse(
        double grossSales,
        double netSales,
        double fees,
        double adSpend,
        double cogs,
        double grossProfit,
        double contributionMargin,
        double marginPct
) {}
