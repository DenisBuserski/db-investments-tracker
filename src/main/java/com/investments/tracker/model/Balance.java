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
@Builder
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "balance") 
    private BigDecimal balance;

    @Column(name = "total_investments")
    private BigDecimal totalInvestments;

    @Column(name = "total_deposits")
    private BigDecimal totalDeposits;

    @Column(name = "total_withdrawals")
    private BigDecimal totalWithdrawals;

    @Column(name = "total_dividends")
    private BigDecimal totalDividends;

    @Column(name = "total_fees")
    private BigDecimal totalFees;

    @Column(name = "last_portfolio_value")
    private BigDecimal lastPortfolioValue;

    @Column(name = "last_unrealized_pl")
    private BigDecimal lastUnrealizedPl;

    @Column(name = "last_unrealized_pl_percentage")
    private BigDecimal lastUnrealizedPlPercentage;

    @Column(name = "total_sold")
    private BigDecimal totalSold;

    @Column(name = "realized_pl")
    private BigDecimal realizedPl;
}
