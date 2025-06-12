package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.BalanceResponseDTO;
import com.investments.tracker.dto.deposit.DepositRequestDTO;
import com.investments.tracker.dto.deposit.DepositResponseDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    private DepositRequestDTO depositRequestDTO;

    private CashTransaction cashTransaction;

    private Balance balance;

    private final LocalDate date = LocalDate.of(2025, 1, 1);

    @BeforeEach
    public void setUp() {
        depositRequestDTO = DepositRequestDTO.builder()
                .date(date)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransaction = CashTransaction.builder()
                .date(date)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        balance = Balance.builder()
                .date(date)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(1000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        cashTransactionRepository.deleteAll();
    }

    @Test
    @DisplayName("Test should insert a successful deposit for the first time")
    public void testInsertSuccessfulDepositForTheFirstTime() {
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(cashTransaction);
        // This line tells Mockito that whenever the "save()" method of cashTransactionRepository is called with any CashTransaction object,
        // it should return the predefined CashTransaction object(Which you would define elsewhere in your test).
        // In other words, you're mocking the repository to simulate that when you save a CashTransaction, it successfully returns the mock object cashTransaction.
        // Since you are testing the "insertDeposit()" method, which interacts with the cashTransactionRepository, you want to simulate the behavior of saving a
        // CashTransaction to avoid needing a real database or performing a real save operation. By mocking it, you can control its return value.
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.empty());

        BalanceResponseDTO balanceResponseDTO = depositService.insertDeposit(depositRequestDTO);
        assertEquals(balanceResponseDTO.getBalance(), BigDecimal.valueOf(1000));
        assertEquals(balanceResponseDTO.getTotalDeposits(), BigDecimal.valueOf(1000));

        verify(cashTransactionRepository).save(any(CashTransaction.class));
        // This line verifies that the "save()" method on cashTransactionRepository was called exactly once with any CashTransaction object.
        // The goal is to ensure that the service method interacts with the cashTransactionRepository as expected, performing a save operation on the CashTransaction.
        verify(balanceRepository).getLatestBalance();
        verify(balanceRepository).save(any(Balance.class));
    }


    @Test
    @DisplayName("Test should create a successful deposit")
    public void testInsertSuccessfulDeposit() {
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(cashTransaction);
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.of(balance));

        BalanceResponseDTO balanceResponseDTO = depositService.insertDeposit(depositRequestDTO);
        assertEquals(0, balanceResponseDTO.getBalance().compareTo(BigDecimal.valueOf(2000)));
        assertEquals(0, balanceResponseDTO.getTotalDeposits().compareTo(BigDecimal.valueOf(2000)));

        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).getLatestBalance();
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we have deposits")
    public void testGetAllDepositsFromToNotEmpty() {
        // when(cashTransactionRepository.getCashTransactionsFromTo(date, date, DEPOSIT)).thenReturn(List.of(cashTransaction));

        List<DepositResponseDTO> result = depositService.getAllDepositsFromTo(date, date);
        assertEquals(1, result.size());

        // verify(cashTransactionRepository, times(1)).getCashTransactionsFromTo(date, date, DEPOSIT);
    }

    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we don't have deposits")
    public void testGetAllDepositsFromToEmpty() {
        // when(cashTransactionRepository.getCashTransactionsFromTo(date, date, DEPOSIT)).thenReturn(Collections.emptyList());

        List<DepositResponseDTO> result = depositService.getAllDepositsFromTo(date, date);
        assertEquals(0, result.size());

        // verify(cashTransactionRepository).getCashTransactionsFromTo(date, date, DEPOSIT);
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we have deposits")
    public void testGetTotalDepositsAmountNotEmpty() {
        // when(balanceRepository.getTotalDepositsAmount()).thenReturn(Optional.of(balance.getTotalDeposits()));

        BigDecimal result = depositService.getTotalDepositsAmount();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));

        // verify(balanceRepository).getTotalDepositsAmount();
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
    public void testGetTotalDepositsAmountEmpty() {
        // when(balanceRepository.getTotalDepositsAmount()).thenReturn(Optional.empty());

        BigDecimal result = depositService.getTotalDepositsAmount();
        assertEquals(0, result.compareTo(BigDecimal.ZERO));

        // verify(balanceRepository).getTotalDepositsAmount();
    }

}
