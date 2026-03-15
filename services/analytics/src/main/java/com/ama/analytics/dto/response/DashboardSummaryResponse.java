package com.ama.analytics.dto.response;

public record DashboardSummaryResponse(
        SummaryMetrics summary,
        PeriodDelta compare
) {
    public record SummaryMetrics(
            double totalSales,
            long totalOrders,
            long totalUnits,
            double adSpend,
            double roas,
            double acos,
            double profit,
            double conversionRate
    ) {}

    public record PeriodDelta(
            double salesDeltaPct,
            double ordersDeltaPct,
            double profitDeltaPct
    ) {}
}
