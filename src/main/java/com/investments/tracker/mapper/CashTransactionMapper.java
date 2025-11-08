package com.investments.tracker.mapper;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.model.CashTransaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;


@Component
public class CashTransactionMapper {
    public <T> CashTransaction createCashtransaction(T dto, Function<T, CashTransaction> mapper) {
        return mapper.apply(dto);
    }

    public CashTransactionResponse mapToResponseDTO(CashTransaction cashTransaction, CashTransactionType cashTransactionType) {
        return new CashTransactionResponse(
                cashTransaction.getDate(),
                cashTransactionType,
                cashTransaction.getAmount(),
                cashTransaction.getCurrency(),
                cashTransaction.getDescription()
        );
    }

    public List<CashTransactionResponse> mapToResponseDTOList(List<CashTransaction> cashTransactions, CashTransactionType cashTransactionType) {
        return cashTransactions
                .stream()
                .map(cashTransaction -> mapToResponseDTO(cashTransaction, cashTransactionType))
                .toList();
    }
}
