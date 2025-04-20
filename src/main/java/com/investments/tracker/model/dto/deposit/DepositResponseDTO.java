package com.investments.tracker.model.dto.deposit;

import com.investments.tracker.model.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DepositResponseDTO {
    private BigDecimal amount;
    private Currency currency;
    private LocalDate date;
    private String description;
}
