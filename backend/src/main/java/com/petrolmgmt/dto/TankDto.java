package com.petrolmgmt.dto;

import java.math.BigDecimal;

public record TankDto(
        Long id,
        String label,
        Long fuelProductId,
        String fuelProductName,
        BigDecimal capacityLiters,
        BigDecimal currentLiters
) {
}
