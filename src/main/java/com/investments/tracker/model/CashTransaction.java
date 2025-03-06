package com.investments.tracker.model;

import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cash_transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CashTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "cash_transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CashTransactionType cashTransactionType;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "description")
    private String description;
}
