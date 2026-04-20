package com.petrolmgmt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "tanks")
public class Tank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fuel_product_id")
    private FuelProduct fuelProduct;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal capacityLiters;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal currentLiters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public FuelProduct getFuelProduct() {
        return fuelProduct;
    }

    public void setFuelProduct(FuelProduct fuelProduct) {
        this.fuelProduct = fuelProduct;
    }

    public BigDecimal getCapacityLiters() {
        return capacityLiters;
    }

    public void setCapacityLiters(BigDecimal capacityLiters) {
        this.capacityLiters = capacityLiters;
    }

    public BigDecimal getCurrentLiters() {
        return currentLiters;
    }

    public void setCurrentLiters(BigDecimal currentLiters) {
        this.currentLiters = currentLiters;
    }
}
