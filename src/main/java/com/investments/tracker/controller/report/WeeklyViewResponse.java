package com.investments.tracker.controller.report;

import com.investments.tracker.service.report.WeeklyProductPositionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WeeklyViewResponse {
    private int weekNumber;
    private LocalDate lastDayOfTheWeek;
    private BigDecimal returnOnInvestment;
    private BigDecimal beggingPortfolioValue;
    private BigDecimal totalInvestedValue;
    private BigDecimal totalUnrealizedProfitLoss;
    private BigDecimal totalUnrealizedProfitLossPercentage;
    private List<WeeklyProductPositionDTO> weeklyPositions;
}
