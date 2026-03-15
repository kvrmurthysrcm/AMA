package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface FlexibleQueryService {
    com.ama.analytics.dto.response.AnalyticsQueryResponse execute(com.ama.analytics.dto.request.AnalyticsQueryRequest request);
}
