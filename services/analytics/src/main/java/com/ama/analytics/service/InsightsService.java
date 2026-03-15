package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface InsightsService {
    java.util.List<com.ama.analytics.dto.response.InsightCard> getSummary(AnalyticsFilterRequest request);
}
