package com.petrolmgmt.service;

import com.petrolmgmt.dto.DashboardStatsDto;
import com.petrolmgmt.repository.FuelProductRepository;
import com.petrolmgmt.repository.SaleRepository;
import com.petrolmgmt.repository.TankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class DashboardService {

    private final SaleRepository saleRepository;
    private final TankRepository tankRepository;
    private final FuelProductRepository fuelProductRepository;

    public DashboardService(
            SaleRepository saleRepository,
            TankRepository tankRepository,
            FuelProductRepository fuelProductRepository
    ) {
        this.saleRepository = saleRepository;
        this.tankRepository = tankRepository;
        this.fuelProductRepository = fuelProductRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto stats() {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        var start = today.atStartOfDay(zone).toInstant();
        var end = today.plusDays(1).atStartOfDay(zone).toInstant();

        BigDecimal totalToday = saleRepository.sumTotalBetween(start, end);
        long salesToday = saleRepository.countBetween(start, end);
        long tanks = tankRepository.count();
        long products = fuelProductRepository.count();

        return new DashboardStatsDto(totalToday, salesToday, tanks, products);
    }
}
