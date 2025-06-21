package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.enums.TransactionType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.PortfolioRepository;
import com.investments.tracker.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponseDTO;
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
            return createBalanceResponseDTO(null);
        } else {
            TransactionType transactionType = transactionRequest.getTransactionType();
            BigDecimal balanceValue = currentBalance.get().getBalance();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequest);

            BalanceResponse balanceResponse = null;
            if (transactionType == BUY) {
                balanceResponse = this.buyTransactionService.insertBuyTransaction(currentBalance.get(), balanceValue, transactionValue, transactionRequest);
            } else if (transactionType == SELL) {
                // balanceResponse = sellTransaction(currentBalance.get(), transactionValue, transactionRequest);
            }
            return balanceResponse;
        }
    }

    private static BigDecimal calculateTransactionValue(TransactionRequest transactionRequestDTO) {
        BigDecimal exchangeRate = transactionRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequestDTO.getExchangeRate();
        BigDecimal singlePrice = transactionRequestDTO.getSinglePrice();
        int quantity = transactionRequestDTO.getQuantity();
        BigDecimal calculationWithoutExchangeRate = singlePrice.multiply(BigDecimal.valueOf(quantity));

        if (exchangeRate.equals(BigDecimal.ZERO)) {
            return calculationWithoutExchangeRate;
        } else {
            return calculationWithoutExchangeRate.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    public static Transaction createTransaction(TransactionRequest transactionRequestDTO, BigDecimal transactionValue) {
        BigDecimal exchangeRate = transactionRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequestDTO.getExchangeRate();
        String description = transactionRequestDTO.getFees().isEmpty() ? "No fees related to this transaction" : "Check 'cashtransaction' table for related fees";
        return Transaction.builder()
                .date(transactionRequestDTO.getDate())
                .transactionType(transactionRequestDTO.getTransactionType())
                .productType(transactionRequestDTO.getProductType())
                .productName(transactionRequestDTO.getProductName())
                .singlePrice(transactionRequestDTO.getSinglePrice())
                .quantity(transactionRequestDTO.getQuantity())
                .exchangeRate(exchangeRate)
                .totalAmount(transactionValue)
                .currency(EUR)
                .baseCurrency(transactionRequestDTO.getCurrency())
                .description(description)
                .build();
    }

}
