package com.investments.tracker.model;

import com.investments.tracker.model.enums.Currency;
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

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "dividend_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal dividendAmount;

    @Column(name = "dividend_tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal dividendTaxAmount;

    @Column(name = "dividend_amount_per_share", nullable = false, precision = 10, scale = 4)
    private BigDecimal dividendAmountPerShare;

    @Column(name = "dividend_tax_amount_per_share", nullable = false, precision = 10, scale = 4)
    private BigDecimal dividendTaxAmountPerShare;

    @Column(name = "exchange_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
