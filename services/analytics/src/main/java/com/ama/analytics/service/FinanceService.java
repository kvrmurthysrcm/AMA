package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface FinanceService {
    com.ama.analytics.dto.response.ProfitabilityResponse getProfitability(AnalyticsFilterRequest request);
}
