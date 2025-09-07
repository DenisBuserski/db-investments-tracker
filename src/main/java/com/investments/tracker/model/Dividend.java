package com.investments.tracker.model;

import com.investments.tracker.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dividends")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Dividend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_amount_after_tax_and_conversion")
    private BigDecimal totalAmountAfterTaxAndConversion;

    @Column(name = "total_amount_after_tax_before_conversion")
    private BigDecimal totalAmountAfterTaxBeforeConversion;

    @Column(name = "base_amount")
    private BigDecimal baseAmount;

    @Column(name = "total_tax_amount")
    private BigDecimal totalTaxAmount;

    @Column(name = "dividend_amount_per_share")
    private BigDecimal amountPerShare;

    @Column(name = "dividend_tax_amount_per_share")
    private BigDecimal taxAmountPerShare;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "dividend_currency")
    @Enumerated(EnumType.STRING)
    private Currency dividendCurrency;
}
