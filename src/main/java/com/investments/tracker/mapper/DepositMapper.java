package com.investments.tracker.mapper;

import com.investments.tracker.dto.response.CashTransactionResponse;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.request.DepositRequest;
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

    public CashTransactionResponse mapToResponseDTO(CashTransaction deposit) {
        return new CashTransactionResponse(
                deposit.getDate(),
                deposit.getAmount(),
                deposit.getCurrency(),
                deposit.getDescription()
        );
    }

    public List<CashTransactionResponse> mapToResponseDTOList(List<CashTransaction> deposits) {
        return deposits.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
