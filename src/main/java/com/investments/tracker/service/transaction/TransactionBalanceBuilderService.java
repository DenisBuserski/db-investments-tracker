package com.investments.tracker.service.transaction;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionBalanceBuilderService {

    public Balance createNewBalanceFromTransaction(Balance balance, Transaction transaction, BigDecimal totalAmountOfInsertedFees) {
        LocalDate newBalanceDate = transaction.getDate();
        BigDecimal newBalanceAmount = balance.getBalance().subtract(transaction.getTotalAmount());
        BigDecimal newTotalInvestments = balance.getTotalInvestments().add(transaction.getTotalAmount());
        BigDecimal newTotalDeposits = balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance.getTotalDividends();
        BigDecimal newTotalFees = balance.getTotalFees().add(totalAmountOfInsertedFees);
        BigDecimal newLastPortfolioValue = balance.getLastPortfolioValue();
        BigDecimal lastUnrealizedPl = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPl();
        BigDecimal lastUnrealizedPlPercentage = balance == null ? BigDecimal.ZERO : balance.getLastUnrealizedPlPercentage();
        BigDecimal totalSold = balance == null ? BigDecimal.ZERO : balance.getTotalSold();
        BigDecimal realizedPl = balance == null ? BigDecimal.ZERO : balance.getRealizedPl();

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
