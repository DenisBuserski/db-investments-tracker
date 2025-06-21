package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.request.WithdrawalRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

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

}
