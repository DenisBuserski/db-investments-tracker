package com.investments.tracker.unit.mapper;

import com.investments.tracker.controller.request.WithdrawalRequest;
import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.model.CashTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
public class WithdrawalMapperTest {
    private WithdrawalMapper withdrawalMapper;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);
    private final LocalDate DATE_2 = LocalDate.of(2025, 1, 2);
    private final String TEST_DESCRIPTION = "TEST DESCRIPTION";

    @BeforeEach
    public void setUp() {
        withdrawalMapper = new WithdrawalMapper();
    }

    @Test
    @DisplayName("Test should map WithdrawalRequest to CashTransaction")
    public void testMapDepositRequestToCashTransaction() {
        WithdrawalRequest request = new WithdrawalRequest(
                DATE,
                BigDecimal.valueOf(100),
                EUR,
                TEST_DESCRIPTION);

        CashTransaction transaction = withdrawalMapper.apply(request);

        assertThat(transaction.getDate()).isEqualTo(request.getDate());
        assertThat(transaction.getCashTransactionType()).isEqualTo(WITHDRAWAL);
        assertThat(transaction.getAmount()).isEqualByComparingTo(request.getAmount());
        assertThat(transaction.getCurrency()).isEqualTo(request.getCurrency());
        assertThat(transaction.getDescription()).isEqualTo(request.getDescription());
        assertThat(transaction.getReferenceId()).isNull();
    }

    @Test
    @DisplayName("Test should map CashTransaction to CashTransactionResponse")
    public void testMapCashTransactionToCashTransactionResponse() {
        CashTransaction withdrawal = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(100))
                .currency(EUR)
                .description(TEST_DESCRIPTION)
                .referenceId(null)
                .build();

        CashTransactionResponse response = withdrawalMapper.mapToResponseDTO(withdrawal);

        assertThat(response.date()).isEqualTo(withdrawal.getDate());
        assertThat(response.type()).isEqualTo(WITHDRAWAL);
        assertThat(response.amount()).isEqualByComparingTo(withdrawal.getAmount());
        assertThat(response.currency()).isEqualTo(withdrawal.getCurrency());
        assertThat(response.description()).isEqualTo(withdrawal.getDescription());
    }

    @Test
    @DisplayName("Test should map list of CashTransaction to list of CashTransactionResponse")
    public void testMapListOfCashTransactionsToListOfCashTransactionResponses() {
        List<CashTransaction> transactions = List.of(
                CashTransaction.builder()
                        .date(DATE)
                        .cashTransactionType(WITHDRAWAL)
                        .amount(BigDecimal.valueOf(100))
                        .currency(EUR)
                        .description(TEST_DESCRIPTION)
                        .referenceId(null)
                        .build(),
                CashTransaction.builder()
                        .date(DATE_2)
                        .cashTransactionType(WITHDRAWAL)
                        .amount(BigDecimal.valueOf(50))
                        .currency(EUR)
                        .description(TEST_DESCRIPTION)
                        .referenceId(null)
                        .build()
        );

        List<CashTransactionResponse> responses = withdrawalMapper.mapToResponseDTOList(transactions);

        assertThat(responses).hasSize(2);

        assertThat(responses.get(0).date()).isEqualTo(DATE);
        assertThat(responses.get(0).amount()).isEqualByComparingTo(BigDecimal.valueOf(100));

        assertThat(responses.get(1).date()).isEqualTo(DATE_2);
        assertThat(responses.get(1).description()).isEqualTo(TEST_DESCRIPTION);
    }
}
