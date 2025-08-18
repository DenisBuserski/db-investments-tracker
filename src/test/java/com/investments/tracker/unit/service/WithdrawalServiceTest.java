package com.investments.tracker.unit.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.controller.withdrawal.WithdrawalRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.deposit.DepositBalanceBuilderService;
import com.investments.tracker.service.deposit.DepositService;
import com.investments.tracker.service.withdrawal.WithdrawalBalanceBuilderService;
import com.investments.tracker.service.withdrawal.WithdrawalService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {
    @InjectMocks
    private WithdrawalService withdrawalService;

    @Mock
    private WithdrawalBalanceBuilderService withdrawalBalanceBuilderService;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private CashTransactionMapper cashTransactionMapper;

    @Mock
    private WithdrawalMapper withdrawalMapper;

    private WithdrawalRequest withdrawalRequest;
    private CashTransactionResponse cashTransactionResponse;
    private CashTransaction cashTransaction;
    private Balance balance;
    private Balance balance2;
    private Balance balance3;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final LocalDate DATE_1 = LocalDate.of(2026, 1, 1);
    private final LocalDate DATE_2 = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        withdrawalRequest = WithdrawalRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .build();

        balance = Balance.builder()
                .date(DATE_1)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        balance2 = Balance.builder()
                .date(DATE_2)
                .balance(BigDecimal.valueOf(100))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        balance3 = Balance.builder()
                .date(DATE_2)
                .balance(BigDecimal.valueOf(5000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should return error when no balance exists")
    void testShouldReturnErrorWhenNoBalanceExists() {
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals("Withdrawal cannot be made, because no balance exists", exception.getReason());
    }

    // Test Withdrawal date cannot be before the latest balance date"
    @Test
    @DisplayName("Test should return error when withdrawal date is before latest balance date")
    void testShouldReturnErrorWhenWithdrawalDateIsBeforeLatestBalanceDate() {
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.of(balance));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals("Withdrawal date cannot be before the latest balance date", exception.getReason());
    }

    // Test You don't have enough money to withdraw. Current balance is [%s %s]
    @Test
    @DisplayName("Test should return error when there is not enough money")
    void testShouldReturnErrorWhenThereIsNoEnoughMoney() {
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.of(balance2));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertTrue(ex.getReason().contains("You don't have enough money to withdraw. Current balance is "));
    }

    // Test Withdrawal for [{} {}] successful
    @Test
    void insertWithdraw_successfulWithdrawal_returnsBalanceResponse() {
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.of(balance3));
        when(cashTransactionMapper.createCashtransaction(request, withdrawalMapper)).thenReturn(withdrawal);
        when(withdrawalBalanceBuilderService.createBalanceFromCashTransaction(latestBalance, withdrawal)).thenReturn(newBalance);

        BalanceResponse response = withdrawalService.insertWithdraw(request);

        // verify that the transaction and new balance are saved
        verify(cashTransactionRepository).save(withdrawal);
        verify(balanceRepository).save(newBalance);

        assertNotNull(response);
    }
}
