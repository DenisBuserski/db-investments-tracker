package com.investments.tracker.controller.response;

import com.investments.tracker.model.Balance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "The response object for the Balance entity")
public class BalanceResponse {
    @Schema(description = "Date of the balance")
    private LocalDate date;

    @Schema(description = "Total balance on the particular date")
    private BigDecimal balance;

    @Schema(description = "Total investments till the particular date")
    private BigDecimal totalInvestments;

    @Schema(description = "Total deposits till the particular date")
    private BigDecimal totalDeposits;

    @Schema(description = "Total withdrawals till the particular date")
    private BigDecimal totalWithdrawals;

    @Schema(description = "Total dividends received till the particular date")
    private BigDecimal totalDividends;

    @Schema(description = "Total fees paid till the particular date")
    private BigDecimal totalFees;

    @Schema(description = "The last portfolio value on the particular date")
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
