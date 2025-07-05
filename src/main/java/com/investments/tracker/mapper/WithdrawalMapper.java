package com.investments.tracker.mapper;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.request.WithdrawalRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;

@Component
public class WithdrawalMapper implements Function<WithdrawalRequest, CashTransaction> {
    @Override
    public CashTransaction apply(WithdrawalRequest withdrawalRequest) {
        return CashTransaction.builder()
                .date(withdrawalRequest.getDate())
                .cashTransactionType(WITHDRAWAL)
                .amount(withdrawalRequest.getAmount())
                .currency(withdrawalRequest.getCurrency())
                .description(withdrawalRequest.getDescription())
                .referenceId(null)
                .build();
    }

    public CashTransactionResponse mapToResponseDTO(CashTransaction withdrawal) {
        return new CashTransactionResponse(
                withdrawal.getDate(),
                WITHDRAWAL,
                withdrawal.getAmount(),
                withdrawal.getCurrency(),
                withdrawal.getDescription()
        );
    }

    public List<CashTransactionResponse> mapToResponseDTOList(List<CashTransaction> withdrawals) {
        return withdrawals.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

}
