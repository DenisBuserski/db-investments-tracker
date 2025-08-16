package com.investments.tracker.unit.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.deposit.DepositBalanceBuilderService;
import com.investments.tracker.service.deposit.DepositService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

    @InjectMocks
    private DepositService depositService;

    @Mock
    private DepositBalanceBuilderService depositBalanceBuilderService;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private CashTransactionMapper cashTransactionMapper;

    @Mock
    private DepositMapper depositMapper;

    private DepositRequest depositRequest;
    private CashTransactionResponse cashTransactionResponse;
    private CashTransaction cashTransaction;
    private Balance balance;
    private Balance balance2;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    public void setUp() {
        depositRequest = DepositRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .build();

        cashTransactionResponse = new CashTransactionResponse(
                DATE,
                DEPOSIT,
                BigDecimal.valueOf(1000),
                EUR,
                "");

        cashTransaction = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("")
                .build();

        balance = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(1000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        balance2 = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(2000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(2000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should insert a successful deposit for the first time")
    public void testInsertSuccessfulDepositForTheFirstTime() {
        when(cashTransactionMapper.createCashtransaction(eq(depositRequest), eq(depositMapper))).thenReturn(cashTransaction);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(cashTransaction);
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(depositBalanceBuilderService.createBalanceFromCashTransaction(isNull(), eq(cashTransaction))).thenReturn(balance);

        BalanceResponse balanceResponse = depositService.insertDeposit(depositRequest);
        assertEquals(balanceResponse.getBalance(), BigDecimal.valueOf(1000));
        assertEquals(balanceResponse.getTotalDeposits(), BigDecimal.valueOf(1000));

        verify(cashTransactionMapper).createCashtransaction(eq(depositRequest), eq(depositMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(depositBalanceBuilderService).createBalanceFromCashTransaction(isNull(), eq(cashTransaction));
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    @DisplayName("Test should create a successful deposit")
    public void testInsertSuccessfulDeposit() {
        when(cashTransactionMapper.createCashtransaction(eq(depositRequest), eq(depositMapper))).thenReturn(cashTransaction);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(cashTransaction);
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(balance));
        when(depositBalanceBuilderService.createBalanceFromCashTransaction(eq(balance), eq(cashTransaction))).thenReturn(balance2);

        BalanceResponse balanceResponse = depositService.insertDeposit(depositRequest);
        assertEquals(0, balanceResponse.getBalance().compareTo(BigDecimal.valueOf(2000)));
        assertEquals(0, balanceResponse.getTotalDeposits().compareTo(BigDecimal.valueOf(2000)));

        verify(cashTransactionMapper).createCashtransaction(eq(depositRequest), eq(depositMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(depositBalanceBuilderService).createBalanceFromCashTransaction(eq(balance), eq(cashTransaction));
        verify(balanceRepository).save(any(Balance.class));
    }


    //
    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we have deposits")
    public void testGetAllDepositsFromToNotEmpty() {
        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(DEPOSIT), eq(DATE), eq(DATE))).thenReturn(List.of(cashTransaction));
        when(cashTransactionMapper.mapToResponseDTOList(eq(List.of(cashTransaction)), eq(DEPOSIT))).thenReturn(List.of(cashTransactionResponse));

        List<CashTransactionResponse> result = depositService.getAllDepositsFromTo(DATE, DATE);
        assertEquals(1, result.size());
        assertEquals(result.get(0).amount(), BigDecimal.valueOf(1000));

        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(DEPOSIT, DATE, DATE);
        verify(cashTransactionMapper).mapToResponseDTOList(List.of(cashTransaction), DEPOSIT);
    }

    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we don't have deposits")
    public void testGetAllDepositsFromToEmpty() {
        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(DEPOSIT), eq(DATE), eq(DATE))).thenReturn(Collections.emptyList());

        List<CashTransactionResponse> result = depositService.getAllDepositsFromTo(DATE, DATE);
        assertEquals(0, result.size());

        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(DEPOSIT, DATE, DATE);
        verifyNoInteractions(depositMapper);
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we have deposits")
    public void testGetTotalDepositsAmountNotEmpty() {
        when(cashTransactionRepository.getTotalAmountOf(DEPOSIT)).thenReturn(Optional.of(balance.getTotalDeposits()));

        BigDecimal result = depositService.getTotalDepositsAmount();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));

        verify(cashTransactionRepository).getTotalAmountOf(DEPOSIT);
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
    public void testGetTotalDepositsAmountEmpty() {
        when(cashTransactionRepository.getTotalAmountOf(DEPOSIT)).thenReturn(Optional.empty());

        BigDecimal result = depositService.getTotalDepositsAmount();
        assertEquals(0, result.compareTo(BigDecimal.ZERO));

        verify(cashTransactionRepository).getTotalAmountOf(DEPOSIT);
    }

}
