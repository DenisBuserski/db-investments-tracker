package com.investments.tracker.service.report;

import com.investments.tracker.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductDetailsDTO {
    private String productName;
    private Long totalQuantity;
    private BigDecimal totalInvestedValue;
    private Currency currency;
}