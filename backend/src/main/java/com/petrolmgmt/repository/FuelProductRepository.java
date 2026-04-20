package com.petrolmgmt.repository;

import com.petrolmgmt.model.FuelProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuelProductRepository extends JpaRepository<FuelProduct, Long> {

    Optional<FuelProduct> findByNameIgnoreCase(String name);
}
