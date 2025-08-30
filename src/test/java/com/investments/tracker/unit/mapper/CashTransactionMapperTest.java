package com.investments.tracker.unit.mapper;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

class CashTransactionMapperTest {
    private CashTransactionMapper cashTransactionMapper;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final LocalDate DATE_2 = LocalDate.of(2025, 1, 2);
    private final String TEST_DESCRIPTION = "TEST DESCRIPTION";

    @BeforeEach
    void setUp() {
        cashTransactionMapper = new CashTransactionMapper();
    }

    @Test
    @DisplayName("Test should map CashTransaction to CashTransactionResponse")
    void testMapCashTransactionToCashTransactionResponse() {
        CashTransaction deposit = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(100))
                .currency(EUR)
                .description(TEST_DESCRIPTION)
                .referenceId(null)
                .build();

        CashTransactionResponse response = cashTransactionMapper.mapToResponseDTO(deposit, DEPOSIT);

        assertThat(response.date()).isEqualTo(deposit.getDate());
        assertThat(response.type()).isEqualTo(DEPOSIT);
        assertThat(response.amount()).isEqualByComparingTo(deposit.getAmount());
        assertThat(response.currency()).isEqualTo(deposit.getCurrency());
        assertThat(response.description()).isEqualTo(deposit.getDescription());
    }

    @Test
    @DisplayName("Test should map list of CashTransaction to list of CashTransactionResponse")
    void testMapListOfCashTransactionsToListOfCashTransactionResponses() {
        List<CashTransaction> transactions = List.of(
                CashTransaction.builder()
                        .date(DATE)
                        .cashTransactionType(DEPOSIT)
                        .amount(BigDecimal.valueOf(100))
                        .currency(EUR)
                        .description(TEST_DESCRIPTION)
                        .referenceId(null)
                        .build(),
                CashTransaction.builder()
                        .date(DATE_2)
                        .cashTransactionType(DEPOSIT)
                        .amount(BigDecimal.valueOf(50))
                        .currency(EUR)
                        .description(TEST_DESCRIPTION)
                        .referenceId(null)
                        .build()
        );

        List<CashTransactionResponse> responses = cashTransactionMapper.mapToResponseDTOList(transactions, DEPOSIT);

        assertThat(responses).hasSize(2);

        assertThat(responses.get(0).date()).isEqualTo(DATE);
        assertThat(responses.get(0).amount()).isEqualByComparingTo(BigDecimal.valueOf(100));

        assertThat(responses.get(1).date()).isEqualTo(DATE_2);
        assertThat(responses.get(1).description()).isEqualTo(TEST_DESCRIPTION);
    }
}
