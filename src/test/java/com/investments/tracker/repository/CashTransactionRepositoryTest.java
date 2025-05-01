package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.Assertions;
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

import static com.investments.tracker.model.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.model.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.model.enums.Currency.EUR;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CashTransactionRepositoryTest {

    @Autowired
    CashTransactionRepository cashTransactionRepository;

    CashTransaction cashTransactionDeposit;

    CashTransaction cashTransactionWithdrawal;

    @BeforeEach
    public void setUp() {
        cashTransactionDeposit = CashTransaction.builder()
                .date(LocalDate.now())
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransactionWithdrawal = CashTransaction.builder()
                .date(LocalDate.now())
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();
    }

    @Test
    @DisplayName("Get all deposits from [date] to [date]")
    public void testGetCashTransactionsFromToDeposits() {
        List<CashTransaction> result = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(DEPOSIT, LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Get all withdrawals from [date] to [date]")
    public void testGetCashTransactionsFromToWithdrawals() {
        List<CashTransaction> result = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(0, result.size());
    }


}
