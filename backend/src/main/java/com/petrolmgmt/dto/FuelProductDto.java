package com.petrolmgmt.dto;

import java.math.BigDecimal;

public record FuelProductDto(Long id, String name, BigDecimal pricePerLiter) {
}
