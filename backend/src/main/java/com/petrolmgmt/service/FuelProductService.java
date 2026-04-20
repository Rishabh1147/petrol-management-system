package com.petrolmgmt.service;

import com.petrolmgmt.dto.CreateFuelProductRequest;
import com.petrolmgmt.dto.FuelProductDto;
import com.petrolmgmt.dto.UpdateFuelProductRequest;
import com.petrolmgmt.model.FuelProduct;
import com.petrolmgmt.repository.FuelProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FuelProductService {

    private final FuelProductRepository fuelProductRepository;

    public FuelProductService(FuelProductRepository fuelProductRepository) {
        this.fuelProductRepository = fuelProductRepository;
    }

    @Transactional(readOnly = true)
    public List<FuelProductDto> listAll() {
        return fuelProductRepository.findAll().stream()
                .map(this::toDto)
                .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
                .toList();
    }

    @Transactional(readOnly = true)
    public FuelProductDto getById(Long id) {
        return fuelProductRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuel product not found"));
    }

    @Transactional
    public FuelProductDto create(CreateFuelProductRequest request) {
        fuelProductRepository.findByNameIgnoreCase(request.name().trim()).ifPresent(p -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A product with this name already exists");
        });
        FuelProduct entity = new FuelProduct();
        entity.setName(request.name().trim());
        entity.setPricePerLiter(request.pricePerLiter());
        return toDto(fuelProductRepository.save(entity));
    }

    @Transactional
    public FuelProductDto update(Long id, UpdateFuelProductRequest request) {
        FuelProduct entity = fuelProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuel product not found"));
        fuelProductRepository.findByNameIgnoreCase(request.name().trim()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A product with this name already exists");
            }
        });
        entity.setName(request.name().trim());
        entity.setPricePerLiter(request.pricePerLiter());
        return toDto(fuelProductRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!fuelProductRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fuel product not found");
        }
        fuelProductRepository.deleteById(id);
    }

    private FuelProductDto toDto(FuelProduct p) {
        return new FuelProductDto(p.getId(), p.getName(), p.getPricePerLiter());
    }
}
