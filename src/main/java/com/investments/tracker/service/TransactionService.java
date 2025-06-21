package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.Currency.EUR;
import static com.investments.tracker.enums.TransactionType.BUY;
import static com.investments.tracker.enums.TransactionType.SELL;


@Service
@Slf4j
public class TransactionService{
    private final BalanceRepository balanceRepository;
    private final BuyTransactionService buyTransactionService;

    @Autowired
    public TransactionService(
            BalanceRepository balanceRepository,
            BuyTransactionService buyTransactionService) {
        this.balanceRepository = balanceRepository;
        this.buyTransactionService = buyTransactionService;
    }

    public BalanceResponse insertTransaction(TransactionRequest transactionRequest) {
        Optional<Balance> currentBalance = this.balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            log.error("Transaction cannot be created because there is no current balance!");
            return createBalanceResponse(null);
        } else {
            TransactionType transactionType = transactionRequest.getTransactionType();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequest);

            BalanceResponse balanceResponse = null;
            if (transactionType == BUY) {
                balanceResponse = this.buyTransactionService.insertBuyTransaction(currentBalance.get(), transactionValue, transactionRequest);
            } else if (transactionType == SELL) {
                // balanceResponse = sellTransaction(currentBalance.get(), transactionValue, transactionRequest);
            }
            return balanceResponse;
        }
    }

    private static BigDecimal calculateTransactionValue(TransactionRequest transactionRequest) {
        BigDecimal exchangeRate = transactionRequest.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequest.getExchangeRate();
        BigDecimal singlePrice = transactionRequest.getSinglePrice();
        int quantity = transactionRequest.getQuantity();
        log.info("Start calculating transaction value with the following params: [SinglePrice:{} | Quantity:{} | ExchangeRate:{}]", singlePrice, quantity, exchangeRate);
        BigDecimal calculationWithoutExchangeRate = singlePrice.multiply(BigDecimal.valueOf(quantity));

        if (exchangeRate.equals(BigDecimal.ZERO)) {
            return calculationWithoutExchangeRate;
        } else {
            return calculationWithoutExchangeRate.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public static Transaction createTransaction(TransactionRequest transactionRequest, BigDecimal transactionValue) {
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
                .baseCurrency(transactionRequest.getCurrency()) // TODO: Rename to base_product_currency
                .description(description)
                .build();
    }

}
