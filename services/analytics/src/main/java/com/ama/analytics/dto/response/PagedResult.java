package com.ama.analytics.dto.response;

import com.ama.common.api.ApiMeta;

import java.util.List;

public record PagedResult<T>(
        List<T> items,
        ApiMeta meta
) {}
