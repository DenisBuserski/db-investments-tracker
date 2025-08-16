package com.investments.tracker.service;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;


public abstract class BalanceBuilder {
    public abstract Balance createBalanceFromCashTransaction(Balance balance, CashTransaction cashTransaction);

    public Balance balanceBuilder(
            LocalDate newBalanceDate,
            BigDecimal newBalanceAmount,
            BigDecimal newTotalInvestments,
            BigDecimal newTotalDeposits,
            BigDecimal newTotalWithdrawals,
            BigDecimal newTotalDividends,
            BigDecimal newTotalFees,
            BigDecimal newLastPortfolioValue,
            BigDecimal lastUnrealizedPl,
            BigDecimal lastUnrealizedPlPercentage,
            BigDecimal totalSold,
            BigDecimal realizedPl) {
        return Balance.builder()
                .date(newBalanceDate)
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .lastUnrealizedPl(lastUnrealizedPl)
                .lastUnrealizedPlPercentage(lastUnrealizedPlPercentage)
                .totalSold(totalSold)
                .realizedPl(realizedPl)
                .build();
    }


}
