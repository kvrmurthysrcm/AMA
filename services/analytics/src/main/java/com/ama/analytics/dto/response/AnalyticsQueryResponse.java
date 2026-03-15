package com.ama.analytics.dto.response;

import java.util.List;
import java.util.Map;

public record AnalyticsQueryResponse(
        List<String> columns,
        List<Map<String, Object>> rows,
        Map<String, Object> totals
) {}
