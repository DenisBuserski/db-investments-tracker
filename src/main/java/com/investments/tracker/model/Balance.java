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
    private LocalDate date;
    private BigDecimal balance;
    private BigDecimal totalInvestments;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalDividends;
    private BigDecimal totalFees;
    private BigDecimal lastPortfolioValue;
    private BigDecimal lastUnrealizedPl;
    private BigDecimal lastUnrealizedPlPercentage;
    private BigDecimal totalSold;
    private BigDecimal realizedPl;
}
