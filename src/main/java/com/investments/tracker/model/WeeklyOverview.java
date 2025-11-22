package com.investments.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_overview")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WeeklyOverview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate lastDayOfTheWeek;
    private int week;
    private BigDecimal ROIPercentage;
    private BigDecimal beggingPortfolioValue;
    private BigDecimal totalInvestedValue;
    private BigDecimal totalUnrealizedProfitLoss;
    private BigDecimal totalUnrealizedProfitLossPercentage;

}
