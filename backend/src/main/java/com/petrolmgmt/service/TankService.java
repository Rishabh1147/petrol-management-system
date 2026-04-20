package com.petrolmgmt.service;

import com.petrolmgmt.dto.CreateTankRequest;
import com.petrolmgmt.dto.TankDto;
import com.petrolmgmt.model.FuelProduct;
import com.petrolmgmt.model.Tank;
import com.petrolmgmt.repository.FuelProductRepository;
import com.petrolmgmt.repository.TankRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TankService {

    private final TankRepository tankRepository;
    private final FuelProductRepository fuelProductRepository;

    public TankService(TankRepository tankRepository, FuelProductRepository fuelProductRepository) {
        this.tankRepository = tankRepository;
        this.fuelProductRepository = fuelProductRepository;
    }

    @Transactional(readOnly = true)
    public List<TankDto> listAll() {
        return tankRepository.findAllWithProduct().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TankDto getById(Long id) {
        return tankRepository.findByIdWithProduct(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tank not found"));
    }

    @Transactional
    public TankDto create(CreateTankRequest request) {
        FuelProduct product = fuelProductRepository.findById(request.fuelProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid fuel product"));
        if (request.currentLiters().compareTo(request.capacityLiters()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current level cannot exceed capacity");
        }
        Tank tank = new Tank();
        tank.setLabel(request.label().trim());
        tank.setFuelProduct(product);
        tank.setCapacityLiters(request.capacityLiters());
        tank.setCurrentLiters(request.currentLiters());
        return toDto(tankRepository.save(tank));
    }

    @Transactional
    public void delete(Long id) {
        if (!tankRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tank not found");
        }
        tankRepository.deleteById(id);
    }

    Tank getEntityForSale(Long tankId, Long expectedProductId) {
        Tank tank = tankRepository.findByIdWithProduct(tankId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid tank"));
        if (!tank.getFuelProduct().getId().equals(expectedProductId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tank does not hold the selected fuel product");
        }
        return tank;
    }

    @Transactional
    public TankDto adjustLevel(Long id, BigDecimal newLevel) {
        Tank tank = tankRepository.findByIdWithProduct(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tank not found"));
        if (newLevel.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level cannot be negative");
        }
        if (newLevel.compareTo(tank.getCapacityLiters()) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level cannot exceed capacity");
        }
        tank.setCurrentLiters(newLevel);
        return toDto(tankRepository.save(tank));
    }

    private TankDto toDto(Tank t) {
        return new TankDto(
                t.getId(),
                t.getLabel(),
                t.getFuelProduct().getId(),
                t.getFuelProduct().getName(),
                t.getCapacityLiters(),
                t.getCurrentLiters()
        );
    }
}
