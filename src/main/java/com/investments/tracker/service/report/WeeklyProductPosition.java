package com.investments.tracker.service.report;

import com.investments.tracker.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyProductPosition {
    private String productName;
    private int quantity;
    private BigDecimal beggingPortfolioValue;
    private BigDecimal openPrice;
    private Currency currency;
    private BigDecimal exchangeRate;
    private BigDecimal totalValue;
    private BigDecimal totalUnrealizedProfitLoss;
    private BigDecimal totalUnrealizedProfitLossPercentage;
}
