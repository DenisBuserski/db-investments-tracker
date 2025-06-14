package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.enums.CashTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {

    List<CashTransaction> findByCashTransactionTypeAndDateBetween(CashTransactionType cashTransactionType, LocalDate from, LocalDate to);

    @Query(""" 
            SELECT SUM(ct.amount) 
            FROM CashTransaction ct 
            WHERE ct.cashTransactionType = :cashTransactionType
           """)
    Optional<BigDecimal> getTotalAmountOf(CashTransactionType cashTransactionType);
}
