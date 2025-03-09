package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.impl.DepositServiceImpl;
import com.investments.tracker.service.impl.WithdrawalServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.model.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.model.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class WithdrawalServiceTest {

    @Autowired
    private WithdrawalServiceImpl withdrawalService;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    private WithdrawalRequestDTO withdrawalRequestDTO;

    private CashTransaction cashTransaction;


    @BeforeEach
    public void setUp() {
        withdrawalRequestDTO = WithdrawalRequestDTO.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransaction = CashTransaction.builder()
                .date(LocalDate.now())
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();
    }

}