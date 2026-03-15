package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface ProductService {
    java.util.List<com.ama.analytics.dto.response.ProductPerformanceRow> getPerformance(AnalyticsPagedRequest request);
}
