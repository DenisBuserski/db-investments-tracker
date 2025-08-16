package com.investments.tracker.mapper;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.deposit.DepositRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

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
}
