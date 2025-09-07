package com.investments.tracker.repository;

import com.investments.tracker.model.Portfolio;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByProductName(String productName);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Portfolio p 
            SET
            p.lastUpdated = :transactionDate,  
            p.quantity = :newQuantity, 
            p.investedMoney = :newInvestedMoney,
            p.averagePrice = :newAveragePrice 
            WHERE p.productName = :productName
            """)
    int updatePortfolioWithBuyTransaction(
            @Param("transactionDate") LocalDate transactionDate,
            @Param("productName") String productName,
            @Param("newQuantity") int newQuantity,
            @Param("newInvestedMoney") BigDecimal newInvestedMoney,
            @Param("newAveragePrice") BigDecimal newAveragePrice);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Portfolio p 
            SET
            p.lastUpdated = :date,  
            p.dividendsAmount = :amount
            WHERE p.productName = :productName
            """)
    int updatePortfolioForDividend(
            @Param("date") LocalDate date,
            @Param("productName") String productName,
            @Param("amount") BigDecimal amount);
}
