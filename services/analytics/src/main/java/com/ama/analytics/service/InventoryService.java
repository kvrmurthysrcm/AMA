package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface InventoryService {
    java.util.List<com.ama.analytics.dto.response.StockoutRiskRow> getStockoutRisk(AnalyticsPagedRequest request);
}
