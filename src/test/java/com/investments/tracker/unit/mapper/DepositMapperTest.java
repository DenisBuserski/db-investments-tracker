package com.investments.tracker.unit.mapper;

import com.investments.tracker.controller.request.DepositRequest;
import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class DepositMapperTest {
    private DepositMapper depositMapper;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final LocalDate DATE_2 = LocalDate.of(2025, 1, 2);
    private final String TEST_DESCRIPTION = "TEST DESCRIPTION";

    @BeforeEach
    public void setUp() {
        depositMapper = new DepositMapper();
    }

    @Test
    @DisplayName("Test should map DepositRequest to CashTransaction")
    public void testMapDepositRequestToCashTransaction() {
        DepositRequest request = new DepositRequest(
                DATE,
                BigDecimal.valueOf(100),
                EUR,
                TEST_DESCRIPTION);

        CashTransaction transaction = depositMapper.apply(request);

        assertThat(transaction.getDate()).isEqualTo(request.getDate());
        assertThat(transaction.getCashTransactionType()).isEqualTo(DEPOSIT);
        assertThat(transaction.getAmount()).isEqualByComparingTo(request.getAmount());
        assertThat(transaction.getCurrency()).isEqualTo(request.getCurrency());
        assertThat(transaction.getDescription()).isEqualTo(request.getDescription());
        assertThat(transaction.getReferenceId()).isNull();
    }

    @Test
    @DisplayName("Test should map CashTransaction to CashTransactionResponse")
    public void testMapCashTransactionToCashTransactionResponse() {
        CashTransaction deposit = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(100))
                .currency(EUR)
                .description(TEST_DESCRIPTION)
                .referenceId(null)
                .build();

        CashTransactionResponse response = depositMapper.mapToResponseDTO(deposit);

        assertThat(response.date()).isEqualTo(deposit.getDate());
        assertThat(response.type()).isEqualTo(DEPOSIT);
        assertThat(response.amount()).isEqualByComparingTo(deposit.getAmount());
        assertThat(response.currency()).isEqualTo(deposit.getCurrency());
        assertThat(response.description()).isEqualTo(deposit.getDescription());
    }

    @Test
    @DisplayName("Test should map list of CashTransaction to list of CashTransactionResponse")
    public void testMapListOfCashTransactionsToListOfCashTransactionResponses() {
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

        List<CashTransactionResponse> responses = depositMapper.mapToResponseDTOList(transactions);

        assertThat(responses).hasSize(2);

        assertThat(responses.get(0).date()).isEqualTo(DATE);
        assertThat(responses.get(0).amount()).isEqualByComparingTo(BigDecimal.valueOf(100));

        assertThat(responses.get(1).date()).isEqualTo(DATE_2);
        assertThat(responses.get(1).description()).isEqualTo(TEST_DESCRIPTION);
    }
}

