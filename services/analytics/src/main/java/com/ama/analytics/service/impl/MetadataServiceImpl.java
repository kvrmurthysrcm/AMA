package com.ama.analytics.service.impl;

import com.ama.analytics.dto.request.MetadataFilterRequest;
import com.ama.analytics.dto.request.TenantRequest;
import com.ama.analytics.dto.response.AccountMetadataItem;
import com.ama.analytics.dto.response.BrandMetadataItem;
import com.ama.analytics.dto.response.CategoryMetadataItem;
import com.ama.analytics.repo.MetadataRepository;
import com.ama.analytics.service.MetadataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataServiceImpl implements MetadataService {
    private final MetadataRepository repository;

    public MetadataServiceImpl(MetadataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AccountMetadataItem> getAccounts(TenantRequest request) {
        return repository.findAccounts(request);
    }

    @Override
    public List<BrandMetadataItem> getBrands(MetadataFilterRequest request) {
        return repository.findBrands(request);
    }

    @Override
    public List<CategoryMetadataItem> getCategories(MetadataFilterRequest request) {
        return repository.findCategories(request);
    }
}
