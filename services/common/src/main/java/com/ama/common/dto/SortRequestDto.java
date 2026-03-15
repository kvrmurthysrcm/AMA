package com.ama.common.dto;

public record SortRequestDto(
        String field,
        String direction
) {
    public String directionOrDefault() {
        return direction == null || direction.isBlank() ? "DESC" : direction.toUpperCase();
    }
}
