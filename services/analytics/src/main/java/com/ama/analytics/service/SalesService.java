package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface SalesService {
    java.util.List<com.ama.analytics.dto.response.SalesTimeSeriesPoint> getTimeseries(AnalyticsFilterRequest request);
    java.util.List<com.ama.analytics.dto.response.ProductSalesRow> getSalesByProduct(AnalyticsPagedRequest request);
}
