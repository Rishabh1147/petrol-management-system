package com.petrolmgmt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PatchTankLevelRequest(
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal currentLiters
) {
}
