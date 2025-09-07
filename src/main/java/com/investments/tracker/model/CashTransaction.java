package com.investments.tracker.model;

import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cash_transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CashTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cash_transaction_type")
    @Enumerated(EnumType.STRING)
    private CashTransactionType cashTransactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_id")
    private Long referenceId;
}
