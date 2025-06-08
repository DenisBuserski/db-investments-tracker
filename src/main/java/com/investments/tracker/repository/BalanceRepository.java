package com.investments.tracker.repository;

import com.investments.tracker.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    @Query("""
           SELECT b
           FROM Balance b
           ORDER BY b.id DESC
           LIMIT 1
           """)
    Optional<Balance> getLatestBalance();

    Optional<Balance> findTopByOrderByIdDesc();


}
