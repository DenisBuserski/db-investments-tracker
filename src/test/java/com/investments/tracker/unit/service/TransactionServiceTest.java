package com.investments.tracker.unit.service;

import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.enums.Currency;
import com.investments.tracker.enums.ProductType;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static com.investments.tracker.enums.Currency.EUR;
import static com.investments.tracker.enums.ProductType.STOCK;
import static com.investments.tracker.enums.TransactionType.BUY;
import static com.investments.tracker.validation.ValidationMessages.TRANSACTION_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private BalanceRepository balanceRepository;

    private TransactionRequest transactionRequest;
    private Balance MOCK_BALANCE_WITH_NOT_ENOUGH_MONEY;

    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        transactionRequest = TransactionRequest.builder()
                .date(DATE)
                .transactionType(BUY)
                .productType(STOCK)
                .productName("APPLE")
                .singlePrice(BigDecimal.TEN)
                .quantity(10)
                .exchangeRate(BigDecimal.ZERO)
                .fees(new HashMap<>())
                .currency(EUR)
                .build();

        MOCK_BALANCE_WITH_NOT_ENOUGH_MONEY = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(50))
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
                () -> transactionService.insertTransaction(transactionRequest));
        assertEquals(TRANSACTION_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST, exception.getReason());
    }

    @Test
    @DisplayName("Test should return error when there is not enough money")
    void testShouldReturnErrorWhenThereIsNoEnoughMoney() {
        when(balanceRepository.getLatestBalance()).thenReturn(Optional.of(MOCK_BALANCE_WITH_NOT_ENOUGH_MONEY));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> transactionService.insertTransaction(transactionRequest));
        assertTrue(exception.getReason().contains("cannot be created because there is not enough money"));
    }
}
