package com.petrolmgmt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTankRequest(
        @NotBlank String label,
        @NotNull Long fuelProductId,
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal capacityLiters,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal currentLiters
) {
}
