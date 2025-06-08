package com.investments.tracker.dto.dividend;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.validation.ValidCurrency;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DividendRequestDTO {
    @NotNull(message = "Dividend date cannot be NULL!")
    @PastOrPresent(message = "Dividend date cannot be in the future!")
    private LocalDate date;

    @NotBlank(message = "Product name cannot be blank or NULL!")
    private String productName;

    @Positive(message = "Quantity must be more than 0!")
    private int quantity;

    @NotNull(message = "Dividend amount cannot be NULL!")
    @Positive(message = "Dividend amount must be more than 0!")
    private BigDecimal dividendAmount;

    @NotNull(message = "Dividend tax amount cannot be NULL!")
    @PositiveOrZero(message = "Dividend tax amount must be more or equal to 0!")
    private BigDecimal dividendTax;

    @NotNull(message = "Exchange rate cannot be NULL!")
    private BigDecimal exchangeRate;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;
}
