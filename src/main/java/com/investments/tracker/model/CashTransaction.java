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
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private CashTransactionType cashTransactionType;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String description;

    private Long referenceId;
}
