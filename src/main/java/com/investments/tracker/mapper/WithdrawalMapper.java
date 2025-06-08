package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.withdraw.WithdrawalRequestDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;

@Component
public class WithdrawalMapper implements Function<WithdrawalRequestDTO, CashTransaction> {
    @Override
    public CashTransaction apply(WithdrawalRequestDTO withdrawalRequestDTO) {
        return CashTransaction.builder()
                .date(withdrawalRequestDTO.getDate())
                .cashTransactionType(WITHDRAWAL)
                .amount(withdrawalRequestDTO.getAmount())
                .currency(withdrawalRequestDTO.getCurrency())
                .description(withdrawalRequestDTO.getDescription())
                .referenceId(null)
                .build();
    }

}
