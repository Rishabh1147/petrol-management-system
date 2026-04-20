package com.petrolmgmt.config;

import com.petrolmgmt.model.FuelProduct;
import com.petrolmgmt.model.Tank;
import com.petrolmgmt.repository.FuelProductRepository;
import com.petrolmgmt.repository.TankRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDemoData(FuelProductRepository fuelProductRepository, TankRepository tankRepository) {
        return args -> {
            if (fuelProductRepository.count() > 0) {
                return;
            }
            FuelProduct petrol = new FuelProduct();
            petrol.setName("Petrol");
            petrol.setPricePerLiter(new BigDecimal("106.50"));
            petrol = fuelProductRepository.save(petrol);

            FuelProduct diesel = new FuelProduct();
            diesel.setName("Diesel");
            diesel.setPricePerLiter(new BigDecimal("94.20"));
            diesel = fuelProductRepository.save(diesel);

            Tank t1 = new Tank();
            t1.setLabel("Tank A — Petrol");
            t1.setFuelProduct(petrol);
            t1.setCapacityLiters(new BigDecimal("20000"));
            t1.setCurrentLiters(new BigDecimal("12500.50"));
            tankRepository.save(t1);

            Tank t2 = new Tank();
            t2.setLabel("Tank B — Diesel");
            t2.setFuelProduct(diesel);
            t2.setCapacityLiters(new BigDecimal("20000"));
            t2.setCurrentLiters(new BigDecimal("9800.00"));
            tankRepository.save(t2);
        };
    }
}
