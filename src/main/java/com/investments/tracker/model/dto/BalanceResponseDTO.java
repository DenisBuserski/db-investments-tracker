package com.investments.tracker.model.dto;

import com.investments.tracker.model.Balance;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BalanceResponseDTO {
    private LocalDate date;
    private BigDecimal balance;
    private BigDecimal totalInvestments;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalDividends;
    private BigDecimal totalFees;
    private BigDecimal lastPortfolioValue;

    public static BalanceResponseDTO createBalanceResponseDTO(Balance newBalance) {
        return BalanceResponseDTO.builder()
                .date(newBalance.getDate())
                .balance(newBalance.getBalance())
                .totalInvestments(newBalance.getTotalInvestments())
                .totalDeposits(newBalance.getTotalDeposits())
                .totalWithdrawals(newBalance.getTotalWithdrawals())
                .totalDividends(newBalance.getTotalDividends())
                .totalFees(newBalance.getTotalFees())
                .lastPortfolioValue(newBalance.getLastPortfolioValue())
                .build();
    }
}
