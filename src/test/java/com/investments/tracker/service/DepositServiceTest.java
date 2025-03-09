package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.impl.DepositServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.model.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.model.enums.Currency.EUR;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class DepositServiceTest {

    @Autowired
    private DepositServiceImpl depositService;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    private DepositRequestDTO depositRequestDTO;

    private CashTransaction cashTransaction;


    @BeforeEach
    public void setUp() {
        depositRequestDTO = DepositRequestDTO.builder()
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

    @AfterEach
    public void cleanUp() {
        cashTransactionRepository.deleteAll();
    }


    @Test
    @DisplayName("Test should create a successful deposit for the first time")
    public void testInsertSuccessfulDepositForTheFirstTime() {
        BalanceResponseDTO balanceResponseDTO = depositService.insertDeposit(depositRequestDTO);
        Assertions.assertEquals(balanceResponseDTO.getBalance(), BigDecimal.valueOf(1000));
        Assertions.assertEquals(balanceResponseDTO.getTotalDeposits(), BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("Test should create a successful deposit")
    public void testInsertSuccessfulDeposit() {
        depositService.insertDeposit(depositRequestDTO);
        BalanceResponseDTO balanceResponseDTO = depositService.insertDeposit(depositRequestDTO);
        Assertions.assertEquals(0, balanceResponseDTO.getBalance().compareTo(BigDecimal.valueOf(2000)));
        Assertions.assertEquals(0, balanceResponseDTO.getTotalDeposits().compareTo(BigDecimal.valueOf(2000)));
    }

    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we have deposits")
    public void testGetAllDepositsFromToNotEmpty() {
        cashTransactionRepository.save(cashTransaction);
        List<DepositResponseDTO> result = depositService.getAllDepositsFromTo(LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Test should return all deposits from [date] to [date] when we don't have deposits")
    public void testGetAllDepositsFromToEmpty() {
        List<DepositResponseDTO> result = depositService.getAllDepositsFromTo(LocalDate.now(), LocalDate.now());
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we have deposits")
    public void testGetTotalDepositsAmountNotEmpty() {
        cashTransactionRepository.save(cashTransaction);
        BigDecimal result = depositService.getTotalDepositsAmount();
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
    public void testGetTotalDepositsAmountEmpty() {
        BigDecimal result = depositService.getTotalDepositsAmount();
        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(0)));
    }

}
