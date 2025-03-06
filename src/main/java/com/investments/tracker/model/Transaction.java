package com.investments.tracker.model;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.enums.ProductType;
import com.investments.tracker.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "single_price", nullable = false)
    private BigDecimal singlePrice;

    private int quantity;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;


}
