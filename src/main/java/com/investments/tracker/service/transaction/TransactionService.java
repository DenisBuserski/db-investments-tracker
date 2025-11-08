package com.investments.tracker.service.transaction;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.enums.Currency.EUR;
import static com.investments.tracker.enums.TransactionType.BUY;
import static com.investments.tracker.enums.TransactionType.SELL;
import static com.investments.tracker.validation.ValidationMessages.TRANSACTION_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST;
import static com.investments.tracker.validation.ValidationMessages.TRANSACTION_NOT_POSSIBLE_NOT_ENOUGH_MONEY;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService{
    private final BalanceRepository balanceRepository;
    private final BuyTransactionService buyTransactionService;
    private final SellTransactionService sellTransactionService;

    @Transactional
    public BalanceResponse insertTransaction(TransactionRequest transactionRequest) {
        Optional<Balance> currentBalance = balanceRepository.getLatestBalance();
        if (currentBalance.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TRANSACTION_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST);
        } else {
            TransactionType transactionType = transactionRequest.getTransactionType();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequest);

            BalanceResponse balanceResponse = null;
            if (transactionType == BUY) {
                if (currentBalance.get().getBalance().compareTo(transactionValue) >= 0) {
                    balanceResponse = buyTransactionService.insertBuyTransaction(currentBalance.get(), transactionValue, transactionRequest);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TRANSACTION_NOT_POSSIBLE_NOT_ENOUGH_MONEY.replace("%1", "BUY"));
                }
            } else if (transactionType == SELL) {
                boolean isValid = sellTransactionService.validateSellTransaction(transactionRequest.getProductName(), transactionValue);
                if (isValid) {
                    balanceResponse = sellTransactionService.insertSellTransaction();
                } else {
                    // SELL transaction cannot be made
                }
            }
            return balanceResponse;
        }
    }

    private static BigDecimal calculateTransactionValue(TransactionRequest transactionRequest) {
        BigDecimal exchangeRate = transactionRequest.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequest.getExchangeRate();
        BigDecimal singlePrice = transactionRequest.getSinglePrice();
        int quantity = transactionRequest.getQuantity();
        Currency currency = transactionRequest.getCurrency();

        log.info("Start calculating transaction value with the following params: [SinglePrice:{} | Quantity:{} | ExchangeRate:{} | Currency:{}]", singlePrice, quantity, exchangeRate, currency.name());
        BigDecimal calculationWithoutExchangeRate = singlePrice.multiply(BigDecimal.valueOf(quantity));

        if (exchangeRate.equals(BigDecimal.ZERO)) {
            return calculationWithoutExchangeRate;
        } else {
            return calculationWithoutExchangeRate.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
        }
    }

}
