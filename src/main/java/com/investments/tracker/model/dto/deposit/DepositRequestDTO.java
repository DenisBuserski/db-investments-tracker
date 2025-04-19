package com.investments.tracker.model.dto.deposit;

import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.validation.ValidCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DepositRequestDTO {
    @NotNull(message = "Date cannot be NULL!!")
    @PastOrPresent(message = "Date cannot be in the future!")
    private LocalDate date;

    @NotNull(message = "Deposit cannot be NULL!!")
    @Positive(message = "Deposit amount must be more than 0!")
    private BigDecimal amount;

    @NotNull(message = "Currency cannot be NULL!!")
    @ValidCurrency
    private Currency currency;

    @NotBlank(message = "Description cannot be blank or NULL!")
    private String description;
}
