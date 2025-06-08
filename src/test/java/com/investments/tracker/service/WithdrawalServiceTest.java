package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.BalanceResponseDTO;
import com.investments.tracker.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class WithdrawalServiceTest {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    private Balance balance;

    private WithdrawalRequestDTO withdrawalRequestDTO;

    private CashTransaction cashTransaction;


    @BeforeEach
    public void setUp() {
        balance = Balance.builder()
                .date(LocalDate.now())
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        withdrawalRequestDTO = WithdrawalRequestDTO.builder()
                .date(LocalDate.now())
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        cashTransaction = CashTransaction.builder()
                .date(LocalDate.now())
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();
    }

    @AfterEach
    public void cleanUp() {
        cashTransactionRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    @DisplayName("Test should create a successful withdrawal")
    public void testSuccessfulWithdrawal() {
        this.balanceRepository.save(balance);
        BalanceResponseDTO balanceResponseDTO = withdrawalService.withdrawCash(withdrawalRequestDTO);
        assertEquals(balanceResponseDTO.getBalance(), BigDecimal.ZERO);
        assertEquals(balanceResponseDTO.getTotalWithdrawals(), BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("Test should not create a withdrawal because balance is not enough")
    public void testUnsuccessfulWithdrawal() {
        this.balanceRepository.save(balance);
        withdrawalService.withdrawCash(withdrawalRequestDTO);
        BalanceResponseDTO balanceResponseDTO = withdrawalService.withdrawCash(withdrawalRequestDTO);
        assertEquals(balanceResponseDTO.getBalance(), BigDecimal.ZERO);
        assertEquals(balanceResponseDTO.getTotalWithdrawals(), BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("Test should return empty BalanceDTO when there is no current balance")
    public void testIncorrectWithdrawal() {
        BalanceResponseDTO balanceResponseDTO = withdrawalService.withdrawCash(withdrawalRequestDTO);
        assertEquals(balanceResponseDTO.getBalance(), BigDecimal.ZERO);
        assertEquals(balanceResponseDTO.getTotalWithdrawals(), BigDecimal.ZERO);
    }



    @Test
    @DisplayName("Test should return all withdrawals from [date] to [date] when we have withdrawals")
    public void testGetAllWithdrawalsFromToNotEmpty() {
        cashTransactionRepository.save(cashTransaction);
        List<WithdrawalResponseDTO> result = withdrawalService.getAllWithdrawalsFromTo(LocalDate.now(), LocalDate.now());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Test should return all withdrawals from [date] to [date] when we don't have withdrawals")
    public void testGetAllWithdrawalsFromToEmpty() {
        List<WithdrawalResponseDTO> result = withdrawalService.getAllWithdrawalsFromTo(LocalDate.now(), LocalDate.now());
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test should return total amount of all withdrawals when we have withdrawals")
    public void testGetTotalWithdrawalsAmountNotEmpty() {
        cashTransactionRepository.save(cashTransaction);
        BigDecimal result = withdrawalService.getTotalWithdrawalsAmount();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));

    }

    @Test
    @DisplayName("Test should return total amount of all withdrawals when we don't have withdrawals")
    public void testGetTotalWithdrawalsAmountEmpty() {
        BigDecimal result = withdrawalService.getTotalWithdrawalsAmount();
        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));
    }

}