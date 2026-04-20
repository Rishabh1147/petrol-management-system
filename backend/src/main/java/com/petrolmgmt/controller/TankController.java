package com.petrolmgmt.controller;

import com.petrolmgmt.dto.CreateTankRequest;
import com.petrolmgmt.dto.PatchTankLevelRequest;
import com.petrolmgmt.dto.TankDto;
import com.petrolmgmt.service.TankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tanks")
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    @GetMapping
    public List<TankDto> list() {
        return tankService.listAll();
    }

    @GetMapping("/{id}")
    public TankDto get(@PathVariable Long id) {
        return tankService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TankDto create(@Valid @RequestBody CreateTankRequest body) {
        return tankService.create(body);
    }

    @PatchMapping("/{id}/level")
    public TankDto patchLevel(@PathVariable Long id, @Valid @RequestBody PatchTankLevelRequest body) {
        return tankService.adjustLevel(id, body.currentLiters());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tankService.delete(id);
    }
}
