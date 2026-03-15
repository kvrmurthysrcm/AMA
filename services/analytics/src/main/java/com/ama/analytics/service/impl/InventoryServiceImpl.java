package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.AnalyticsPagedRequest;
import com.ama.analytics.dto.response.StockoutRiskRow;
import com.ama.analytics.repo.InventoryAnalyticsRepository;
import com.ama.analytics.service.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryAnalyticsRepository repository;
    private final AnalyticsValidationService validationService;

    public InventoryServiceImpl(InventoryAnalyticsRepository repository, AnalyticsValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    @Override
    public List<StockoutRiskRow> getStockoutRisk(AnalyticsPagedRequest request) {
        validationService.validatePagedRequest(request);
        return repository.fetchStockoutRisk(request);
    }
}
