package com.investments.tracker.service;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.request.DividendRequest;
import com.investments.tracker.enums.CashTransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.enums.Currency.EUR;

@Service
public class CashTransactionService {
    public CashTransaction createCashTransactionForDividend(DividendRequest dividendRequest, BigDecimal dividendAmount) {
        String dividendDescription = String.format("Dividend for product [%s]", dividendRequest.getProductName());
        return CashTransaction.builder()
                .date(dividendRequest.getDate())
                .cashTransactionType(DIVIDEND)
                .amount(dividendAmount)
                .currency(EUR)
                .description(dividendDescription)
                .referenceId(null)
                .build();
    }

    public CashTransaction createCashTransactionForFee(LocalDate date, CashTransactionType cashTransactionType, String feeType, BigDecimal feeValue, long transactionId) {
        String feeDescription = String.format("Fee of type %s; Reference id to 'transaction' table", feeType);
        return CashTransaction.builder()
                .date(date)
                .cashTransactionType(cashTransactionType)
                .amount(feeValue)
                .currency(EUR)
                .description(feeDescription)
                .referenceId(transactionId)
                .build();
    }

    public List<CashTransactionResponse> getAllCashTransactionsFromTo(LocalDate from, LocalDate to) {
        return null;
    }

    public BigDecimal getAllCashTransactionsAmount() {
        return null;
    }
}
