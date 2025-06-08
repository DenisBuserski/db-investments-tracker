package com.investments.tracker.model;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "weekly_positions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WeeklyPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(name = "total_invested_cash", nullable = false)
    private BigDecimal totalInvestmentCash;

    @Column(name = "open_price", nullable = false)
    private long openPrice;

    @Column(name = "product_base_currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency productBaseCurrency;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "investment_value", nullable = false)
    private BigDecimal investmentValue;

    @Column(name = "unrealized_PL", nullable = false)
    private BigDecimal unrealizedProfitLoss;

    @Column(name = "unrealized_PL_percentage", nullable = false)
    private BigDecimal unrealizedProfitLossPercentage;
}
