package com.investments.tracker.dto.withdraw;

import com.investments.tracker.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class WithdrawalResponseDTO {
    private BigDecimal amount;
    private Currency currency;
    private LocalDate date;
    private String description;
}
