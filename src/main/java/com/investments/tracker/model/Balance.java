package com.investments.tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "balance")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    private BigDecimal balance;

    @Column(name = "total_investments", nullable = false)
    private BigDecimal totalInvestments;

    @Column(name = "total_deposits", nullable = false)
    private BigDecimal totalDeposits;

    @Column(name = "total_withdrawals", nullable = false)
    private BigDecimal totalWithdrawals;

    @Column(name = "total_dividends", nullable = false)
    private BigDecimal totalDividends;

    @Column(name = "total_fees", nullable = false)
    private BigDecimal totalFees;
}
