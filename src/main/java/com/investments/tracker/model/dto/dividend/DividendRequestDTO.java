package com.investments.tracker.model.dto.dividend;

import com.investments.tracker.model.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DividendRequestDTO {
    private LocalDate date;
    private String productName;
    private int quantity;
    private BigDecimal dividendAmount;
    private BigDecimal exchangeRate;
    private Currency currency;
}
