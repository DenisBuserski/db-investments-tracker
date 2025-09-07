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

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "single_price")
    private BigDecimal singlePrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "base_product_currency")
    @Enumerated(EnumType.STRING)
    private Currency baseProductCurrency;

    @Column(name = "description")
    private String description;
}
