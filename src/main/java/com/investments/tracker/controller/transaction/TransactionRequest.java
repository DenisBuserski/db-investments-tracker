package com.investments.tracker.controller.transaction;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.enums.FeeType;
import com.investments.tracker.enums.ProductType;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.validation.ValidCurrency;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static com.investments.tracker.validation.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TransactionRequest {
    @NotNull(message = DATE_NOT_NULL)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    private LocalDate date;

    @NotNull(message = TRANSACTION_TYPE_NOT_NULL)
    private TransactionType transactionType;

    @NotNull(message = PRODUCT_TYPE_NOT_NULL)
    private ProductType productType;

    @NotBlank(message = PRODUCT_NAME_NOT_BLANK_OR_NULL)
    private String productName;

    @NotNull(message = TRANSACTION_PRICE_NOT_NULL)
    @Positive(message = TRANSACTION_PRICE_MORE_THAN_ZERO)
    private BigDecimal singlePrice;

    @Positive(message = QUANTITY_MORE_THAN_ZERO)
    private int quantity;

    @NotNull(message = EXCHANGE_RATE_NOT_NULL)
    private BigDecimal exchangeRate;

    private Map<
            @NotNull(message = FEE_TYPE_NOT_NULL) FeeType,
            @NotNull(message = FEE_AMOUNT_NOT_NULL) BigDecimal> fees;

    @NotNull(message = CURRENCY_NOT_NULL)
    @ValidCurrency
    private Currency currency;
}
