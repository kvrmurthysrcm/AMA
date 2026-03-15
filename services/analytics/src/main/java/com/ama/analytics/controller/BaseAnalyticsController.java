package com.ama.analytics.controller;

import com.ama.common.api.ApiMeta;
import com.ama.common.api.ApiResponse;
import com.ama.platform.util.TraceIdHolder;

public abstract class BaseAnalyticsController {
    protected <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.success(message, TraceIdHolder.getTraceId(), data);
    }

    protected <T> ApiResponse<T> ok(String message, T data, ApiMeta meta) {
        return ApiResponse.success(message, TraceIdHolder.getTraceId(), data, meta);
    }
}
