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
    private LocalDate date;
    private String productName;
    private int quantity;
    private BigDecimal totalAmountAfterTaxAndConversion;
    private BigDecimal totalAmountAfterTaxBeforeConversion;
    private BigDecimal baseAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal amountPerShare;
    private BigDecimal taxAmountPerShare;
    private BigDecimal exchangeRate;

    @Enumerated(EnumType.STRING)
    private Currency dividendCurrency;
}
