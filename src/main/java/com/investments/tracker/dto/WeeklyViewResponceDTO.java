package com.investments.tracker.model.dto;

import com.investments.tracker.model.WeeklyPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyViewResponceDTO {
    private int year;
    private int weekNumber;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal returnOnInvestment;
    private BigDecimal beggingPortfolioValue;
    private BigDecimal totalInvestmentValue;
    private BigDecimal totalUnrealizedProfitLoss;
    private BigDecimal totalUnrealizedProfitLossPercentage;
    private List<WeeklyPosition> weeklyPositions;
}
