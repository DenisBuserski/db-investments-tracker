package com.investments.tracker.service;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.*;

@Service
@Slf4j
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }







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

        return balanceBuilder(newBalanceDate, newBalanceAmount, newTotalInvestments, newTotalDeposits, newTotalWithdrawals, newTotalDividends, newTotalFees, newLastPortfolioValue, lastUnrealizedPl, lastUnrealizedPlPercentage, totalSold, realizedPl);
    }

    public static Balance balanceBuilder(LocalDate newBalanceDate,
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

    public BalanceResponse getLatestBalanceData(LocalDateTime dateTime) {
        Optional<Balance> latestBalance = this.balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            return createBalanceResponse(latestBalance.get());
        }
        log.info("No balance found for [{}]", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return createBalanceResponse(null);
    }
}
