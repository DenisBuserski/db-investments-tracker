package com.investments.tracker.unit.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.controller.withdrawal.WithdrawalRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
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

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static com.investments.tracker.validation.ValidationMessages.WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE;
import static com.investments.tracker.validation.ValidationMessages.WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
    private CashTransaction withdrawal;
    private Balance MOCK_BALANCE_WITH_ENOUGH_MONEY;
    private Balance MOCK_BALANCE_WITH_ENOUGH_MONEY_AFTER_WITHDRAWAL;
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

        withdrawal = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("")
                .build();

        cashTransactionResponse = new CashTransactionResponse(
                DATE,
                WITHDRAWAL,
                BigDecimal.valueOf(1000),
                EUR,
                "");

        MOCK_BALANCE_WITH_ENOUGH_MONEY = Balance.builder()
                .date(DATE_2)
                .balance(BigDecimal.valueOf(5000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        MOCK_BALANCE_WITH_ENOUGH_MONEY_AFTER_WITHDRAWAL = Balance.builder()
                .date(DATE_2)
                .balance(BigDecimal.valueOf(4000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.valueOf(1000))
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should return error when no balance exists")
    void testShouldReturnErrorWhenNoBalanceExists() {
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals(WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST, exception.getReason());
    }

    @Test
    @DisplayName("Test should return error when withdrawal date is before latest balance date")
    void testShouldReturnErrorWhenWithdrawalDateIsBeforeLatestBalanceDate() {
        Balance MOCK_BALANCE_WITH_DATE_AFTER_WITHDRAWAL = Balance.builder()
                .date(DATE_1)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(MOCK_BALANCE_WITH_DATE_AFTER_WITHDRAWAL));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals(WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE, exception.getReason());
    }


    @Test
    @DisplayName("Test should return error when there is not enough money")
    void testShouldReturnErrorWhenThereIsNoEnoughMoney() {
        Balance MOCK_BALANCE_WITH_NOT_ENOUGH_MONEY = Balance.builder()
                .date(DATE_2)
                .balance(BigDecimal.valueOf(100))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(MOCK_BALANCE_WITH_NOT_ENOUGH_MONEY));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertTrue(ex.getReason().contains("You don't have enough money to withdraw. Current balance is "));
    }

    @Test
    @DisplayName("Test should create successful withdrawal")
    void testShouldCreateSuccessfulWithdrawal() {
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(MOCK_BALANCE_WITH_ENOUGH_MONEY));
        when(cashTransactionMapper.createCashtransaction(withdrawalRequest, withdrawalMapper)).thenReturn(withdrawal);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(withdrawal);
        when(withdrawalBalanceBuilderService.createBalanceFromCashTransaction(eq(MOCK_BALANCE_WITH_ENOUGH_MONEY), eq(withdrawal))).thenReturn(MOCK_BALANCE_WITH_ENOUGH_MONEY_AFTER_WITHDRAWAL);

        BalanceResponse balanceResponse = withdrawalService.insertWithdraw(withdrawalRequest);
        assertEquals(0, balanceResponse.getBalance().compareTo(BigDecimal.valueOf(4000)));
        assertEquals(0, balanceResponse.getTotalWithdrawals().compareTo(BigDecimal.valueOf(1000)));

        verify(cashTransactionMapper).createCashtransaction(eq(withdrawalRequest), eq(withdrawalMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(withdrawalBalanceBuilderService).createBalanceFromCashTransaction(eq(MOCK_BALANCE_WITH_ENOUGH_MONEY), eq(withdrawal));
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    @DisplayName("Test should return all withdrawals from [date] to [date] when we have withdrawals")
    void testGetAllWithdrawalsFromToNotEmpty() {
        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(WITHDRAWAL), eq(DATE), eq(DATE))).thenReturn(List.of(withdrawal));
        when(cashTransactionMapper.mapToResponseDTOList(eq(List.of(withdrawal)), eq(WITHDRAWAL))).thenReturn(List.of(cashTransactionResponse));

        List<CashTransactionResponse> result = withdrawalService.getAllWithdrawalsFromTo(DATE, DATE);
        assertEquals(1, result.size());
        assertEquals(result.get(0).amount(), BigDecimal.valueOf(1000));

        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(WITHDRAWAL, DATE, DATE);
        verify(cashTransactionMapper).mapToResponseDTOList(List.of(withdrawal), WITHDRAWAL);
    }

    @Test
    @DisplayName("Test should return all withdrawals from [date] to [date] when we don't have withdrawals")
    void testGetAllWithdrawalsFromToEmpty() {
        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(WITHDRAWAL), eq(DATE), eq(DATE))).thenReturn(Collections.emptyList());

        List<CashTransactionResponse> result = withdrawalService.getAllWithdrawalsFromTo(DATE, DATE);
        assertEquals(0, result.size());

        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(WITHDRAWAL, DATE, DATE);
        verifyNoInteractions(withdrawalMapper);
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we have deposits")
    void testGetTotalWithdrawalsAmountNotEmpty() {
        when(cashTransactionRepository.getTotalAmountOf(WITHDRAWAL)).thenReturn(Optional.of(MOCK_BALANCE_WITH_ENOUGH_MONEY_AFTER_WITHDRAWAL.getTotalWithdrawals()));

        BigDecimal result = withdrawalService.getTotalWithdrawalsAmount();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));

        verify(cashTransactionRepository).getTotalAmountOf(WITHDRAWAL);
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
    void testGetTotalWithdrawalsAmountEmpty() {
        when(cashTransactionRepository.getTotalAmountOf(WITHDRAWAL)).thenReturn(Optional.empty());

        BigDecimal result = withdrawalService.getTotalWithdrawalsAmount();
        assertEquals(0, result.compareTo(BigDecimal.ZERO));

        verify(cashTransactionRepository).getTotalAmountOf(WITHDRAWAL);
    }
}
