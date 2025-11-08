package com.investments.tracker.controller.deposit;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.validation.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DepositRequest {
    @NotNull(message = DATE_NOT_NULL)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    private LocalDate date;

    @NotNull(message = DEPOSIT_AMOUNT_NOT_NULL)
    @Positive(message = DEPOSIT_AMOUNT_MORE_THAN_ZERO)
    private BigDecimal amount;

    @NotNull(message = CURRENCY_NOT_NULL)
    @ValidCurrency
    private Currency currency;

    @NotNull(message = DESCRIPTION_NOT_NULL)
    private String description;
}
