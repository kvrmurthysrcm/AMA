package com.ama.analytics.dto.response;

public record ProductPerformanceRow(
        long productId,
        String sku,
        String asin,
        String productName,
        double netSales,
        long unitsSold,
        long sessions,
        double adSpend,
        double roas,
        int closingStockQty,
        double estimatedProfit
) {}
