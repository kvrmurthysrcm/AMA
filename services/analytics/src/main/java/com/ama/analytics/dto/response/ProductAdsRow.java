package com.ama.analytics.dto.response;

public record ProductAdsRow(
        long productId,
        String sku,
        String productName,
        double spend,
        long clicks,
        double cpc,
        double attributedSales,
        double acos,
        double roas
) {}
