package com.ama.analytics.query;

public final class AnalyticsSql {
    private AnalyticsSql() {
    }

    public static final String BASE_JOIN = """
            FROM ama.product p
            LEFT JOIN ama.brand b ON b.brand_id = p.brand_id
            LEFT JOIN ama.category c ON c.category_id = p.category_id
            """;
}
