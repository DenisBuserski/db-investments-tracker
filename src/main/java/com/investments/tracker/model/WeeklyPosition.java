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
@Builder
@Getter
public class WeeklyPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "year")
    private int year;

    @Column(name = "week_number")
    private int weekNumber;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private BigDecimal quantity;

    @Column(name = "total_invested_cash")
    private BigDecimal totalInvestedCash;

    @Column(name = "open_price")
    private long openPrice;

    @Column(name = "product_base_currency")
    @Enumerated(EnumType.STRING)
    private Currency productBaseCurrency;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "investment_value")
    private BigDecimal investmentValue;

    @Column(name = "unrealized_PL")
    private BigDecimal unrealizedProfitLoss;

    @Column(name = "unrealized_PL_percentage")
    private BigDecimal unrealizedProfitLossPercentage;
}
