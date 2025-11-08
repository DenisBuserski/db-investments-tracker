package com.investments.tracker.unit.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.service.withdrawal.WithdrawalBalanceBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WithdrawalBalanceBuilderServiceTest {
    @InjectMocks
    private WithdrawalBalanceBuilderService withdrawalBalanceBuilderService;

    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @Test
    @DisplayName("Test should create new balance successfully")
    void testShouldCreateNewBalanceSuccessfully() {
        Balance balance = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(3000))
                .totalDeposits(BigDecimal.valueOf(3000))
                .totalInvestments(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .lastUnrealizedPl(BigDecimal.ZERO)
                .lastUnrealizedPlPercentage(BigDecimal.ZERO)
                .totalSold(BigDecimal.ZERO)
                .realizedPl(BigDecimal.ZERO)
                .build();
        CashTransaction withdrawal = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(2000))
                .currency(EUR)
                .description("")
                .referenceId(null)
                .build();
        Balance result = withdrawalBalanceBuilderService.createBalanceFromCashTransaction(balance, withdrawal);

        assertEquals(result.getBalance(), BigDecimal.valueOf(1000));
        assertEquals(result.getTotalWithdrawals(), BigDecimal.valueOf(2000));
        assertEquals(result.getDate(), DATE);
    }

}
