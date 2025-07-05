package com.investments.tracker.service.withdrawal;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.service.BalanceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@Slf4j
public class WithdrawalBalanceBuilderService extends BalanceBuilder {
    @Override
    public Balance createBalanceFromCashTransaction(Balance balance, CashTransaction withdrawal) {
        LocalDate newBalanceDate = withdrawal.getDate();
        BigDecimal newBalanceAmount = balance.getBalance().subtract(withdrawal.getAmount());
        BigDecimal newTotalInvestments = balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance.getTotalWithdrawals().add(withdrawal.getAmount());
        BigDecimal newTotalDividends = balance.getTotalDividends();
        BigDecimal newTotalFees = balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance.getLastPortfolioValue();
        BigDecimal lastUnrealizedPl = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPl();
        BigDecimal lastUnrealizedPlPercentage = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPlPercentage();
        BigDecimal totalSold = balance == null ? BigDecimal.ZERO : balance.getTotalSold();
        BigDecimal realizedPl = balance == null ? BigDecimal.ZERO : balance.getRealizedPl();

        return balanceBuilder(
                newBalanceDate,
                newBalanceAmount,
                newTotalInvestments,
                newTotalDeposits,
                newTotalWithdrawals,
                newTotalDividends,
                newTotalFees,
                newLastPortfolioValue,
                lastUnrealizedPl,
                lastUnrealizedPlPercentage,
                totalSold,
                realizedPl);
    }
}
