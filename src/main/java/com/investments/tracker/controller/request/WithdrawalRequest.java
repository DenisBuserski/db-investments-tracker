package com.investments.tracker.controller.request;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.common.validation.ValidCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.common.util.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class WithdrawalRequest {
    @NotNull(message = WITHDRAWAL_DATE_NOT_NULL)
    @PastOrPresent(message = WITHDRAWAL_DATE_NOT_IN_FUTURE)
    private LocalDate date;

    @NotNull(message = WITHDRAWAL_AMOUNT_NOT_NULL)
    @Positive(message = WITHDRAWAL_AMOUNT_MORE_THAN_ZERO)
    private BigDecimal amount;

    @NotNull(message = CURRENCY_NOT_NULL)
    @ValidCurrency
    private Currency currency;

    @NotBlank(message = WITHDRAWAL_DESCRIPTION_NOT_BLANK_OR_NULL)
    private String description;
}
