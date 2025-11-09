package com.investments.tracker.model;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.enums.ProductType;
import com.investments.tracker.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private String productName;
    private BigDecimal singlePrice;
    private int quantity;
    private BigDecimal exchangeRate;
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Currency baseProductCurrency;

    private String description;
}
