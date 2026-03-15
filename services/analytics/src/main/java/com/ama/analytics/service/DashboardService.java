package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface DashboardService {
    DashboardSummaryResponse getSummary(AnalyticsFilterRequest request);
    java.util.List<com.ama.analytics.dto.response.DashboardTrendPoint> getTrends(AnalyticsFilterRequest request);
}
