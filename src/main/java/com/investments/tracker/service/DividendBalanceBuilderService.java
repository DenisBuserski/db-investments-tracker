package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.service.BalanceService.balanceBuilder;

@Service
@Slf4j
public class DividendBalanceBuilderService implements BalanceBuilder {
    @Override
    public Balance createNewBalanceFromCashTransaction(Balance balance, CashTransaction dividend) {
        LocalDate newBalanceDate = dividend.getDate();
        BigDecimal newBalanceAmount = balance == null ? dividend.getAmount() : balance.getBalance().add(dividend.getAmount());
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? BigDecimal.ZERO : balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? dividend.getAmount() : balance.getTotalDividends().add(dividend.getAmount());
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();
        BigDecimal lastUnrealizedPl = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPl();
        BigDecimal lastUnrealizedPlPercentage = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPlPercentage();
        BigDecimal totalSold = balance == null ? BigDecimal.ZERO : balance.getTotalSold();
        BigDecimal realizedPl = balance == null ? BigDecimal.ZERO : balance.getRealizedPl();

        return balanceBuilder(
                newBalanceDate,
                newBalanceAmount,
                newTotalInvestments,
                newTotalDeposits, newTotalWithdrawals,
                newTotalDividends,
                newTotalFees,
                newLastPortfolioValue,
                lastUnrealizedPl,
                lastUnrealizedPlPercentage,
                totalSold,
                realizedPl);
    }
}
