package com.investments.tracker.repository;

import com.investments.tracker.model.Transaction;
import com.investments.tracker.service.report.ProductDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
           SELECT new com.investments.tracker.service.report.ProductDetailsDTO(
                      t.productName, SUM(t.quantity), SUM(t.totalAmount), t.baseProductCurrency)
           FROM Transaction t
           GROUP BY t.productName
           """)
    Set<ProductDetailsDTO> findDistinctProductDetails();
}
