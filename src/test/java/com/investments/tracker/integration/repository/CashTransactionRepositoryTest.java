package com.investments.tracker.integration.repository;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.repository.CashTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CashTransactionRepositoryTest {

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    CashTransaction cashTransactionDeposit;
    CashTransaction cashTransactionWithdrawal;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    public void setUp() {
        cashTransactionDeposit = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransactionWithdrawal = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransactionRepository.save(cashTransactionDeposit);
        cashTransactionRepository.save(cashTransactionWithdrawal);
    }

    @Test
    @DisplayName("Get all deposits from [date] to [date]")
    public void testGetCashTransactionsFromToDeposits() {
        List<CashTransaction> result = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(DEPOSIT, DATE, DATE);
        assertEquals(1, result.size());
        assertEquals(DEPOSIT, result.get(0).getCashTransactionType());
    }

    @Test
    @DisplayName("Get all withdrawals from [date] to [date]")
    public void testGetCashTransactionsFromToWithdrawals() {
        List<CashTransaction> result = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, DATE, DATE);
        assertEquals(1, result.size());
        assertEquals(WITHDRAWAL, result.get(0).getCashTransactionType());
    }

    @Test
    @DisplayName("Get all total deposit amount")
    public void testGetAllTotalDepositAmount() {
        Optional<BigDecimal> totalDepositsAmount = this.cashTransactionRepository.getTotalAmountOf(DEPOSIT);
        BigDecimal result = totalDepositsAmount.get();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));
    }



}
