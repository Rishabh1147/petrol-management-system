package com.petrolmgmt.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record SaleDto(
        Long id,
        Long fuelProductId,
        String fuelProductName,
        Long tankId,
        String tankLabel,
        BigDecimal liters,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        Instant soldAt
) {
}
