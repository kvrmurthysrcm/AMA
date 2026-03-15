package com.ama.common.api;

public record ApiMeta(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages
) {
    public static ApiMeta empty() {
        return new ApiMeta(null, null, null, null);
    }
}
