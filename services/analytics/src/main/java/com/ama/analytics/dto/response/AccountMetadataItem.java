package com.ama.analytics.dto.response;

public record AccountMetadataItem(
        long accountId,
        String marketplaceCode,
        String marketplaceName,
        String sellerAccountRef,
        String connectionStatus
) {}
