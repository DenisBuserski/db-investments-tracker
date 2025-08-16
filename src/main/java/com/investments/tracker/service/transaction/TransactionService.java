package com.investments.tracker.service.transaction;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public BalanceResponse insertTransaction(TransactionRequest transactionRequest) {
        Optional<Balance> currentBalance = balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction cannot be created, because no balance exists");
        } else {
            TransactionType transactionType = transactionRequest.getTransactionType();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequest);

            BalanceResponse balanceResponse = null;
            if (transactionType == BUY) {
                if (currentBalance.get().getBalance().compareTo(transactionValue) >= 0) {
                    balanceResponse = buyTransactionService.insertBuyTransaction(currentBalance.get(), transactionValue, transactionRequest);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction of type [BUY] cannot be created because there is not enough money");
                }
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
        log.info("Start calculating transaction value with the following params: [SinglePrice:{} | Quantity:{} | ExchangeRate:{}]", singlePrice, quantity, exchangeRate); // TODO: Specify currencies
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
                .baseProductCurrency(transactionRequest.getCurrency())
                .description(description)
                .build();
    }

}
