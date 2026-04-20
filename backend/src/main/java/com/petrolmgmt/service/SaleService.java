package com.petrolmgmt.service;

import com.petrolmgmt.dto.CreateSaleRequest;
import com.petrolmgmt.dto.SaleDto;
import com.petrolmgmt.model.FuelProduct;
import com.petrolmgmt.model.Sale;
import com.petrolmgmt.model.Tank;
import com.petrolmgmt.repository.FuelProductRepository;
import com.petrolmgmt.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final FuelProductRepository fuelProductRepository;
    private final TankService tankService;

    public SaleService(
            SaleRepository saleRepository,
            FuelProductRepository fuelProductRepository,
            TankService tankService
    ) {
        this.saleRepository = saleRepository;
        this.fuelProductRepository = fuelProductRepository;
        this.tankService = tankService;
    }

    @Transactional(readOnly = true)
    public List<SaleDto> listRecent() {
        return saleRepository.findAllOrderBySoldAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public SaleDto create(CreateSaleRequest request) {
        FuelProduct product = fuelProductRepository.findById(request.fuelProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid fuel product"));
        Tank tank = tankService.getEntityForSale(request.tankId(), request.fuelProductId());

        BigDecimal liters = request.liters().setScale(2, RoundingMode.HALF_UP);
        if (liters.compareTo(tank.getCurrentLiters()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough fuel in the selected tank");
        }

        BigDecimal unitPrice = product.getPricePerLiter();
        BigDecimal total = liters.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        tank.setCurrentLiters(tank.getCurrentLiters().subtract(liters));

        Sale sale = new Sale();
        sale.setFuelProduct(product);
        sale.setTank(tank);
        sale.setLiters(liters);
        sale.setUnitPrice(unitPrice);
        sale.setTotalAmount(total);
        sale.setSoldAt(Instant.now());

        Sale saved = saleRepository.save(sale);
        return toDto(saved);
    }

    private SaleDto toDto(Sale s) {
        return new SaleDto(
                s.getId(),
                s.getFuelProduct().getId(),
                s.getFuelProduct().getName(),
                s.getTank() != null ? s.getTank().getId() : null,
                s.getTank() != null ? s.getTank().getLabel() : null,
                s.getLiters(),
                s.getUnitPrice(),
                s.getTotalAmount(),
                s.getSoldAt()
        );
    }
}
