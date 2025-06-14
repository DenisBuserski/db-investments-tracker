package com.investments.tracker.dto;

import com.investments.tracker.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CashTransactionResponse (
        BigDecimal amount,
        Currency currency,
        LocalDate date,
        String description) {}