package com.investments.tracker.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BalanceResponseDTO {
    private LocalDate date;
    private BigDecimal balance;
    private BigDecimal totalInvestments;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalDividends;
    private BigDecimal totalFees;
    private BigDecimal lastPortfolioValue;
}
