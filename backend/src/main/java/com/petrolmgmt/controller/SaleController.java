package com.petrolmgmt.controller;

import com.petrolmgmt.dto.CreateSaleRequest;
import com.petrolmgmt.dto.SaleDto;
import com.petrolmgmt.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<SaleDto> list() {
        return saleService.listRecent();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleDto create(@Valid @RequestBody CreateSaleRequest body) {
        return saleService.create(body);
    }
}
