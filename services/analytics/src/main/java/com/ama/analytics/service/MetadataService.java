package com.ama.analytics.service;

import com.ama.analytics.dto.request.*;
import com.ama.analytics.dto.response.*;

public interface MetadataService {
    java.util.List<com.ama.analytics.dto.response.AccountMetadataItem> getAccounts(com.ama.analytics.dto.request.TenantRequest request);
    java.util.List<com.ama.analytics.dto.response.BrandMetadataItem> getBrands(com.ama.analytics.dto.request.MetadataFilterRequest request);
    java.util.List<com.ama.analytics.dto.response.CategoryMetadataItem> getCategories(com.ama.analytics.dto.request.MetadataFilterRequest request);
}
