package com.investments.tracker.dto.response;

import com.investments.tracker.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashTransactionResponse (
        LocalDate date,
        BigDecimal amount,
        Currency currency,
        String description) {}