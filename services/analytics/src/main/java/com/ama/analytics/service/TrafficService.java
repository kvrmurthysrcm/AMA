package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface TrafficService {
    com.ama.analytics.dto.response.TrafficConversionResponse getConversion(AnalyticsFilterRequest request);
}
