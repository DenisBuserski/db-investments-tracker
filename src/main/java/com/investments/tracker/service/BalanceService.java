package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BalanceService {

    public Balance createNewBalanceFromDeposit(Balance balance, CashTransaction deposit) {
        LocalDate newBalanceDate = deposit.getDate();
        BigDecimal newBalanceAmount = balance == null ? deposit.getAmount() : balance.getBalance().add(deposit.getAmount());
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? deposit.getAmount() : balance.getTotalDeposits().add(deposit.getAmount());
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? BigDecimal.ZERO : balance.getTotalDividends();
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();

        return balanceBuilder(newBalanceDate, newBalanceAmount, newTotalInvestments, newTotalDeposits, newTotalWithdrawals, newTotalDividends, newTotalFees, newLastPortfolioValue);
    }

    public Balance createNewBalanceFromWithdrawal(Balance balance, CashTransaction withdrawal) {
        LocalDate newBalanceDate = withdrawal.getDate();
        BigDecimal newBalanceAmount = balance.getBalance().subtract(withdrawal.getAmount());
        BigDecimal newTotalInvestments = balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance.getTotalWithdrawals().add(withdrawal.getAmount());
        BigDecimal newTotalDividends = balance.getTotalDividends();
        BigDecimal newTotalFees = balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance.getLastPortfolioValue();

        return balanceBuilder(newBalanceDate, newBalanceAmount, newTotalInvestments, newTotalDeposits, newTotalWithdrawals, newTotalDividends, newTotalFees, newLastPortfolioValue);
    }

    public Balance createNewBalanceFromDividend(Balance balance, CashTransaction dividend) {
        LocalDate newBalanceDate = dividend.getDate();
        BigDecimal newBalanceAmount = balance == null ? dividend.getAmount() : balance.getBalance().add(dividend.getAmount());
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? BigDecimal.ZERO : balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? dividend.getAmount() : balance.getTotalDividends().add(dividend.getAmount());
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();

        return balanceBuilder(newBalanceDate, newBalanceAmount, newTotalInvestments, newTotalDeposits, newTotalWithdrawals, newTotalDividends, newTotalFees, newLastPortfolioValue);
    }

    private static Balance balanceBuilder(LocalDate newBalanceDate, BigDecimal newBalanceAmount, BigDecimal newTotalInvestments, BigDecimal newTotalDeposits, BigDecimal newTotalWithdrawals, BigDecimal newTotalDividends, BigDecimal newTotalFees, BigDecimal newLastPortfolioValue) {
        return Balance.builder()
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
