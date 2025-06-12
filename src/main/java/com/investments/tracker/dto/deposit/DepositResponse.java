package com.investments.tracker.dto.deposit;

import com.investments.tracker.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
public record DepositResponse(
        BigDecimal amount,
        Currency currency,
        LocalDate date,
        String description) {}
