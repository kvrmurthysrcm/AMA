package com.ama.analytics.constants;

import java.util.Set;

public final class AnalyticsConstants {
    public static final Set<String> ALLOWED_GRANULARITIES = Set.of("DAY", "WEEK", "MONTH");
    public static final Set<String> ALLOWED_QUERY_METRICS = Set.of(
            "grossSales", "netSales", "unitsSold", "ordersCount", "adSpend", "adSales", "roas", "acos", "sessions", "pageViews"
    );
    public static final Set<String> ALLOWED_QUERY_DIMENSIONS = Set.of(
            "date", "brandName", "categoryName", "productName", "marketplaceCode", "accountId"
    );

    private AnalyticsConstants() {
    }
}
