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

    @Column(precision = 10, scale = 2) // 10 total digits, 2 after the decimal
    private BigDecimal balance;

    @Column(name = "total_investments", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalInvestments;

    @Column(name = "total_deposits", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDeposits;

    @Column(name = "total_withdrawals", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalWithdrawals;

    @Column(name = "total_dividends", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDividends;

    @Column(name = "total_fees", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFees;

    @Column(name = "last_portfolio_value", nullable = false, precision = 10, scale = 3)
    private BigDecimal lastPortfolioValue;
}
