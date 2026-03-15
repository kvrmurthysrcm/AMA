package com.ama.common.api;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        String traceId,
        Instant timestamp,
        T data,
        ApiMeta meta
) {
    public static <T> ApiResponse<T> success(String message, String traceId, T data, ApiMeta meta) {
        return new ApiResponse<>(true, message, traceId, Instant.now(), data, meta);
    }

    public static <T> ApiResponse<T> success(String message, String traceId, T data) {
        return success(message, traceId, data, ApiMeta.empty());
    }

    public static <T> ApiResponse<T> failure(String message, String traceId, T data) {
        return new ApiResponse<>(false, message, traceId, Instant.now(), data, ApiMeta.empty());
    }
}
