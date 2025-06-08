package com.investments.tracker.dto.withdraw;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.validation.ValidCurrency;
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
@Builder
public class WithdrawalRequestDTO {
    @NotNull(message = "Withdrawal date cannot be NULL!")
    @PastOrPresent(message = "Withdrawal date cannot be in the future!")
    private LocalDate date;

    @NotNull(message = "Withdrawal amount cannot be NULL!")
    @Positive(message = "Withdrawal amount must be more than 0!")
    private BigDecimal amount;

    @NotNull(message = "Currency cannot be NULL!")
    @ValidCurrency
    private Currency currency;

    @NotBlank(message = "Withdrawal description cannot be blank or NULL!")
    private String description;
}
