package com.ama.common.dto;

import jakarta.validation.constraints.Min;

public record PageRequestDto(
        @Min(0) Integer page,
        @Min(1) Integer size
) {
    public int pageOrDefault() {
        return page == null ? 0 : page;
    }

    public int sizeOrDefault() {
        return size == null ? 20 : size;
    }
}
