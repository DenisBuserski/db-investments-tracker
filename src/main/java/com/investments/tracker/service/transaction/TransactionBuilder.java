package com.investments.tracker.service.transaction;

import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.investments.tracker.enums.Currency.EUR;

@Component
public class TransactionBuilder {

    public Transaction createBuyTransaction(TransactionRequest transactionRequest, BigDecimal transactionValue) {
        BigDecimal exchangeRate = transactionRequest.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequest.getExchangeRate();
        String description = transactionRequest.getFees().isEmpty() ? "No fees related to this transaction" : "Check 'cashtransaction' table for related fees";

        return Transaction.builder()
                .date(transactionRequest.getDate())
                .transactionType(transactionRequest.getTransactionType())
                .productType(transactionRequest.getProductType())
                .productName(transactionRequest.getProductName())
                .singlePrice(transactionRequest.getSinglePrice())
                .quantity(transactionRequest.getQuantity())
                .exchangeRate(exchangeRate)
                .totalAmount(transactionValue)
                .currency(EUR)
                .baseProductCurrency(transactionRequest.getCurrency())
                .description(description)
                .build();
    }
}
