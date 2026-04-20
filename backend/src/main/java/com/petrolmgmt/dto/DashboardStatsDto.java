package com.petrolmgmt.dto;

import java.math.BigDecimal;

public record DashboardStatsDto(
        BigDecimal totalSalesToday,
        long saleCountToday,
        long tankCount,
        long productCount
) {
}
