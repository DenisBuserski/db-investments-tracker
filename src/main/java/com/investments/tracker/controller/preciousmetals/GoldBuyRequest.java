package com.investments.tracker.controller.preciousmetals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;

import static com.investments.tracker.validation.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GoldBuyRequest {
    @NotNull(message = SELLER_NOT_NULL)
    private String sellerName;

    @NotBlank(message = PRODUCT_NAME_NOT_BLANK_OR_NULL)
    private String productName;

    private URL url;

    @NotNull(message = DATE_NOT_NULL)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    private LocalDate transactionDate;

    @Positive(message = SIZE_MORE_THAN_ZERO)
    private double sizeInGrams;

    @NotNull(message = PRICE_NOT_NULL)
    private BigDecimal priceBGN;

    @NotNull(message = PRICE_NOT_NULL)
    @Positive(message = PRICE_MORE_THAN_ZERO)
    private BigDecimal priceEUR;

    @NotNull(message = PRICE_NOT_NULL)
    @Positive(message = PRICE_MORE_THAN_ZERO)
    private BigDecimal pricePerGramOnDateEUR;
}
