package com.investments.tracker.model.dto;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class DepositDTO {
    @NotNull(message = "Deposit cannot be NULL!")
    @Positive(message = "Deposit amount must be more than 0!")
    private BigDecimal amount;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;

}
