package com.investments.tracker.repository;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.enums.CashTransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.model.enums.CashTransactionType.DEPOSIT;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CashTransactionRepositoryTest {

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    @Test
    @DisplayName("Get all deposits from [date] to [date]")
    public void testGetCashTransactionsFromToDeposits() {
        List<CashTransaction> result = this.cashTransactionRepository.getCashTransactionsFromTo(LocalDate.now(), LocalDate.now(), DEPOSIT);
        Assertions.assertEquals(0, result.size());

    }
}
