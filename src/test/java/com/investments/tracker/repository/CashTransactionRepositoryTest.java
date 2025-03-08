package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.model.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.model.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.model.enums.Currency.EUR;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CashTransactionRepositoryTest {

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

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
        List<CashTransaction> result = this.cashTransactionRepository.getCashTransactionsFromTo(LocalDate.now(), LocalDate.now(), DEPOSIT);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Get all withdrawals from [date] to [date]")
    public void testGetCashTransactionsFromToWithdrawals() {
        List<CashTransaction> result = this.cashTransactionRepository.getCashTransactionsFromTo(LocalDate.now(), LocalDate.now(), WITHDRAWAL);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Get total amount of deposits")
    public void testGetTotalAmountOfDeposits() {
        this.cashTransactionRepository.save(cashTransactionDeposit);
        Optional<BigDecimal> result = this.cashTransactionRepository.getTotalAmountOf(DEPOSIT);
        Assertions.assertEquals(0, result.get().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    @DisplayName("Get total amount of withdrawals")
    public void testGetTotalAmountOfWithdrawals() {
        this.cashTransactionRepository.save(cashTransactionWithdrawal);
        Optional<BigDecimal> result = this.cashTransactionRepository.getTotalAmountOf(WITHDRAWAL);
        Assertions.assertEquals(0, result.get().compareTo(BigDecimal.valueOf(1000)));
    }
}
