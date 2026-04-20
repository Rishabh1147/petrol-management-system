package com.petrolmgmt.repository;

import com.petrolmgmt.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s JOIN FETCH s.fuelProduct LEFT JOIN FETCH s.tank ORDER BY s.soldAt DESC")
    List<Sale> findAllOrderBySoldAtDesc();

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.soldAt >= :from AND s.soldAt < :to")
    BigDecimal sumTotalBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.soldAt >= :from AND s.soldAt < :to")
    long countBetween(@Param("from") Instant from, @Param("to") Instant to);
}
