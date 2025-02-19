package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {

    @Query("""
           SELECT t
           FROM CashTransaction t
           WHERE
           t.cashTransactionType = 'DEPOSIT'
           AND t.date >= :from
           AND t.date <= :to
           """)
    List<CashTransaction> getDepositsFromTo(LocalDate from, LocalDate to);

    // TODO: Get all deposits

    // TODO: Get withdrawals from to

    // TODO: Get all withdrawals

    // TODO: Get all dividends
}
