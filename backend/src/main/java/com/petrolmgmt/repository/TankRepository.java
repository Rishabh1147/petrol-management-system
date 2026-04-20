package com.petrolmgmt.repository;

import com.petrolmgmt.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TankRepository extends JpaRepository<Tank, Long> {

    @Query("SELECT t FROM Tank t JOIN FETCH t.fuelProduct")
    List<Tank> findAllWithProduct();

    @Query("SELECT t FROM Tank t JOIN FETCH t.fuelProduct WHERE t.id = :id")
    java.util.Optional<Tank> findByIdWithProduct(@Param("id") Long id);
}
