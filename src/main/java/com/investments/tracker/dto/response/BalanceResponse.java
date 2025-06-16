package com.investments.tracker.dto.response;

import com.investments.tracker.model.Balance;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BalanceResponse {
    private LocalDate date;
    private BigDecimal balance;
    private BigDecimal totalInvestments;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalDividends;
    private BigDecimal totalFees;
    private BigDecimal lastPortfolioValue;

    public static BalanceResponse createBalanceResponseDTO(Balance newBalance) {
        LocalDate newBalanceDate = newBalance == null ? LocalDate.now() : newBalance.getDate();
        BigDecimal newBalanceAmount = newBalance == null ? BigDecimal.ZERO : newBalance.getBalance();
        BigDecimal newTotalInvestments = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalInvestments();
        BigDecimal newTotalDeposits = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalWithdrawals();
        BigDecimal newTotalDividends = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDividends();
        BigDecimal newTotalFees = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalFees();
        BigDecimal newLastPortfolioValue = newBalance == null ? BigDecimal.ZERO : newBalance.getLastPortfolioValue();

        return BalanceResponse.builder()
                .date(newBalanceDate)
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .build();
    }
}
