package com.ama.common.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DateRangeDto(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
