package com.investments.tracker.model.dto;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WithdrawalRequestDTO {
    @NotNull(message = "Date cannot be NULL!")
    @PastOrPresent(message = "Date cannot be in the future!")
    private LocalDate date;

    @NotNull(message = "Withdrawal cannot be NULL!")
    @Positive(message = "Withdrawal amount must be more than 0!")
    private BigDecimal amount;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;

    @NotNull(message = "Description cannot be NULL!")
    private String description;
}
