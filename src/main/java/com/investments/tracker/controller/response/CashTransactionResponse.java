package com.investments.tracker.controller.response;

import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "The response object for the CashTransaction entity")
public record CashTransactionResponse (
        @Schema(description = "Date of the cash transaction")
        LocalDate date,

        @Schema(description = "Type of the cash transaction")
        CashTransactionType type,

        @Schema(description = "Amount of the cash transaction")
        BigDecimal amount,

        @Schema(description = "Currency of the cash transaction")
        Currency currency,

        @Schema(description = "Description of the cash transaction")
        String description) {}