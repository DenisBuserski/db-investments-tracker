package com.investments.tracker.model.dto.transaction;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.enums.FeeType;
import com.investments.tracker.model.enums.ProductType;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.model.validation.ValidCurrency;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class TransactionRequestDTO {
    @NotNull(message = "Transaction date cannot be NULL!")
    @PastOrPresent(message = "Transaction date cannot be in the future!")
    private LocalDate date;

    @NotNull(message = "Transaction type cannot be NULL!")
    private TransactionType transactionType;

    @NotNull(message = "Product type cannot be NULL!")
    private ProductType productType;

    @NotBlank(message = "Product name cannot be blank or NULL!")
    private String productName;

    @NotNull(message = "Transaction price cannot be NULL!")
    @Positive(message = "Transaction price must be more than 0!")
    private BigDecimal singlePrice;

    @Positive(message = "Quantity must be more than 0!")
    private int quantity;

    @NotNull(message = "Exchange rate cannot be NULL!")
    private BigDecimal exchangeRate;

    // Check if validation is needed
    private Map<FeeType, BigDecimal> fees;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;
}
