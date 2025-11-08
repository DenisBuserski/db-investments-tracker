package com.investments.tracker.unit.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.service.deposit.DepositBalanceBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DepositBalanceBuilderServiceTest {
    @InjectMocks
    private DepositBalanceBuilderService depositBalanceBuilderService;

    private CashTransaction deposit;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        deposit = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(2000))
                .currency(EUR)
                .description("")
                .referenceId(null)
                .build();
    }

    @Test
    @DisplayName("Test should create new balance when previous balance is null")
    void testShouldCreateNewBalanceWhenPreviousBalanceIsNull() {
        Balance result = depositBalanceBuilderService.createBalanceFromCashTransaction(null, deposit);

        assertEquals(result.getBalance(), BigDecimal.valueOf(2000));
        assertEquals(result.getTotalDeposits(), BigDecimal.valueOf(2000));
        assertEquals(result.getDate(), DATE);
    }

    @Test
    @DisplayName("Test should create new balance when previous balance is not null")
    void testShouldCreateNewBalanceWhenPreviousBalanceIsNotNull() {
        Balance existingBalance = Balance.builder()
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

        Balance result = depositBalanceBuilderService.createBalanceFromCashTransaction(existingBalance, deposit);

        assertEquals(result.getBalance(), BigDecimal.valueOf(5000));
        assertEquals(result.getTotalDeposits(), BigDecimal.valueOf(5000));
        assertEquals(result.getDate(), DATE);
    }
}

