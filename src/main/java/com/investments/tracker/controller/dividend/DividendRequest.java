package com.investments.tracker.controller.dividend;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.common.validation.ValidCurrency;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.common.util.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DividendRequest {
    @NotNull(message = DIVIDEND_DATE_NOT_NULL)
    @PastOrPresent(message = DIVIDEND_DATE_NOT_IN_FUTURE)
    private LocalDate date;

    @NotBlank(message = PRODUCT_NAME_NOT_BLANK_OR_NULL)
    private String productName;

    @Positive(message = QUANTITY_MORE_THAN_ZERO)
    private int quantity;

    @NotNull(message = DIVIDEND_AMOUNT_NOT_NULL)
    @Positive(message = DIVIDEND_AMOUNT_MORE_THAN_ZERO)
    private BigDecimal totalDividendReceived;

    @NotNull(message = DIVIDEND_TAX_AMOUNT_NOT_NULL)
    @PositiveOrZero(message = DIVIDEND_TAX_AMOUNT_MORE_THAN_ZERO)
    private BigDecimal totalDividendTaxCharged;

    @NotNull(message = EXCHANGE_RATE_NOT_NULL)
    private BigDecimal exchangeRate;

    @NotNull(message = CURRENCY_NOT_NULL)
    @ValidCurrency
    private Currency currency;
}
