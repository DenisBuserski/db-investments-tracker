package com.investments.tracker.model.dto;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.enums.ProductType;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.model.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class TransactionRequestDTO {
    @NotNull(message = "Date cannot be NULL!")
    private LocalDate date;

    @NotNull(message = "Transaction type cannot be NULL!")
    private TransactionType transactionType;

    @NotNull(message = "Product type cannot be NULL!")
    private ProductType productType;

    @NotNull(message = "Product name cannot be NULL!")
    private String productName;

    @NotNull(message = "Single price cannot be NULL!")
    @Positive(message = "Single price must be more than 0!")
    private BigDecimal singlePrice;

    @Positive(message = "Quantity must be more than 0!")
    private int quantity;

    @NotNull(message = "Exchange rate cannot be NULL!")
    private BigDecimal exchangeRate;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;
}
