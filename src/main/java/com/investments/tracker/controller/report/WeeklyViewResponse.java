package com.investments.tracker.controller.report;

import com.investments.tracker.service.report.WeeklyProductPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyViewResponse {
    private int weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal returnOnInvestment;
    private BigDecimal beggingPortfolioValue;
    private BigDecimal totalInvestedValue;
    private BigDecimal totalUnrealizedProfitLoss;
    private BigDecimal totalUnrealizedProfitLossPercentage;
    private List<WeeklyProductPosition> weeklyPositions;
}
