package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface AdsService {
    com.ama.analytics.dto.response.AdsOverviewResponse getOverview(AnalyticsFilterRequest request);
    java.util.List<com.ama.analytics.dto.response.ProductAdsRow> getByProduct(AnalyticsPagedRequest request);
}
