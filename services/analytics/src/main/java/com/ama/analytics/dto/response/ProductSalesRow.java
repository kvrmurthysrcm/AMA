package com.ama.analytics.dto.response;

public record ProductSalesRow(
        long productId,
        String sku,
        String asin,
        String productName,
        long unitsSold,
        long orders,
        double netSales,
        double avgSellingPrice
) {}
