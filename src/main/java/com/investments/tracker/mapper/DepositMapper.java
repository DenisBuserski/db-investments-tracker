package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.deposit.DepositRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;

@Component
public class DepositMapper implements Function<DepositRequest, CashTransaction> {
    @Override
    public CashTransaction apply(DepositRequest depositRequest) {
        return CashTransaction.builder()
                .date(depositRequest.getDate())
                .cashTransactionType(DEPOSIT)
                .amount(depositRequest.getAmount())
                .currency(depositRequest.getCurrency())
                .description(depositRequest.getDescription())
                .referenceId(null)
                .build();
    }

    public DepositResponse mapToResponseDTO(CashTransaction deposit) {
        return DepositResponse.builder()
                .date(deposit.getDate())
                .amount(deposit.getAmount())
                .currency(deposit.getCurrency())
                .description(deposit.getDescription())
                .build();
    }

    public List<DepositResponse> mapToResponseDTOList(List<CashTransaction> deposits) {
        return deposits.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
