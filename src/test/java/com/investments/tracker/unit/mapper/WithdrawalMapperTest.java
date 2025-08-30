package com.investments.tracker.unit.mapper;

import com.investments.tracker.controller.withdrawal.WithdrawalRequest;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

class WithdrawalMapperTest {
    private WithdrawalMapper withdrawalMapper;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        withdrawalMapper = new WithdrawalMapper();
    }

    @Test
    @DisplayName("Test should map DepositRequest to CashTransaction")
    void testMapWithdrawalRequestToCashTransaction() {
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(100))
                .currency(EUR)
                .build();

        CashTransaction cashTransaction = withdrawalMapper.apply(withdrawalRequest);

        assertThat(cashTransaction.getDate()).isEqualTo(withdrawalRequest.getDate());
        assertThat(cashTransaction.getCashTransactionType()).isEqualTo(WITHDRAWAL);
        assertThat(cashTransaction.getAmount()).isEqualByComparingTo(withdrawalRequest.getAmount());
        assertThat(cashTransaction.getCurrency()).isEqualTo(withdrawalRequest.getCurrency());
        assertThat(cashTransaction.getReferenceId()).isNull();
    }
}
