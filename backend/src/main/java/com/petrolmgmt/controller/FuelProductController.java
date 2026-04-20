package com.petrolmgmt.controller;

import com.petrolmgmt.dto.CreateFuelProductRequest;
import com.petrolmgmt.dto.FuelProductDto;
import com.petrolmgmt.dto.UpdateFuelProductRequest;
import com.petrolmgmt.service.FuelProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fuel-products")
public class FuelProductController {

    private final FuelProductService fuelProductService;

    public FuelProductController(FuelProductService fuelProductService) {
        this.fuelProductService = fuelProductService;
    }

    @GetMapping
    public List<FuelProductDto> list() {
        return fuelProductService.listAll();
    }

    @GetMapping("/{id}")
    public FuelProductDto get(@PathVariable Long id) {
        return fuelProductService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FuelProductDto create(@Valid @RequestBody CreateFuelProductRequest body) {
        return fuelProductService.create(body);
    }

    @PutMapping("/{id}")
    public FuelProductDto update(@PathVariable Long id, @Valid @RequestBody UpdateFuelProductRequest body) {
        return fuelProductService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        fuelProductService.delete(id);
    }
}
