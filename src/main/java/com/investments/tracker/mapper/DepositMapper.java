package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.deposit.DepositRequestDTO;
import com.investments.tracker.dto.deposit.DepositResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;

@Component
public class DepositMapper implements Function<DepositRequestDTO, CashTransaction> {
    @Override
    public CashTransaction apply(DepositRequestDTO depositRequestDTO) {
        return CashTransaction.builder()
                .date(depositRequestDTO.getDate())
                .cashTransactionType(DEPOSIT)
                .amount(depositRequestDTO.getAmount())
                .currency(depositRequestDTO.getCurrency())
                .description(depositRequestDTO.getDescription())
                .referenceId(null)
                .build();
    }

    public DepositResponseDTO mapToResponseDTO(CashTransaction deposit) {
        return DepositResponseDTO.builder()
                .date(deposit.getDate())
                .amount(deposit.getAmount())
                .currency(deposit.getCurrency())
                .description(deposit.getDescription())
                .build();
    }

    public List<DepositResponseDTO> mapToResponseDTOList(List<CashTransaction> deposits) {
        return deposits.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
