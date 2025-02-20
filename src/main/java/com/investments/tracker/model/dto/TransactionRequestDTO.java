package com.investments.tracker.model.dto;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.enums.ProductType;
import com.investments.tracker.model.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class TransactionRequestDTO {
    private LocalDate date;
    private TransactionType transactionType;
    private ProductType productType;
    private String productName;
    private BigDecimal singlePrice;
    private BigDecimal quantity;
    private BigDecimal exchangeRate;
    private Currency currency;
}
