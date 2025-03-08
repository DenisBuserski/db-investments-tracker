package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.enums.CashTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {

    @Query("""
           SELECT t
           FROM CashTransaction t
           WHERE
           t.cashTransactionType = :cashTransactionType
           AND t.date >= :from
           AND t.date <= :to
           """)
    List<CashTransaction> getCashTransactionsFromTo(LocalDate from, LocalDate to, CashTransactionType cashTransactionType);

    @Query("""
           SELECT SUM(t.amount) FROM CashTransaction t WHERE t.cashTransactionType = :cashTransactionType
           """)
    Optional<BigDecimal> getTotalAmountOf(CashTransactionType cashTransactionType);
}
