package com.petrolmgmt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateSaleRequest(
        @NotNull Long fuelProductId,
        @NotNull Long tankId,
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal liters
) {
}
