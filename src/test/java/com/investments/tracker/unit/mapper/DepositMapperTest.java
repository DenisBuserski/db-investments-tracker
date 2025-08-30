package com.investments.tracker.unit.mapper;

import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

class DepositMapperTest {
    private DepositMapper depositMapper;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        depositMapper = new DepositMapper();
    }

    @Test
    @DisplayName("Test should map DepositRequest to CashTransaction")
    void testMapDepositRequestToCashTransaction() {
        DepositRequest depositRequest = DepositRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(100))
                .currency(EUR)
                .build();

        CashTransaction cashTransaction = depositMapper.apply(depositRequest);

        assertThat(cashTransaction.getDate()).isEqualTo(depositRequest.getDate());
        assertThat(cashTransaction.getCashTransactionType()).isEqualTo(DEPOSIT);
        assertThat(cashTransaction.getAmount()).isEqualByComparingTo(depositRequest.getAmount());
        assertThat(cashTransaction.getCurrency()).isEqualTo(depositRequest.getCurrency());
        assertThat(cashTransaction.getReferenceId()).isNull();
    }
}

