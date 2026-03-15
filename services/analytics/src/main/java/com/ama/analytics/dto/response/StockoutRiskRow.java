package com.ama.analytics.dto.response;

public record StockoutRiskRow(
        long productId,
        String sku,
        String productName,
        int availableQty,
        int inboundQty,
        int reservedQty,
        double avgDailySales,
        double daysOfCover,
        String riskLevel
) {}
