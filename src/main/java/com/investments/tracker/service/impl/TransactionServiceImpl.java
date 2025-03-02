package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.TransactionRequestDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public void insertTransaction(TransactionRequestDTO transactionDTO) {
        // Check if we have enough money in Balance
        Optional<Balance> currentBalance = this.balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            log.error("There is no current balance. Transaction cannot be created.");
        } else {
            BigDecimal balanceValue = currentBalance.get().getBalance();
            BigDecimal transactionValue = transactionDTO.getSinglePrice().multiply(transactionDTO.getQuantity());
            if (balanceValue.compareTo(transactionValue) >= 0) {
                Transaction transaction = createTransaction(transactionDTO);
                this.transactionRepository.save(transaction);
                // Take money from the Balance

            } else {
                log.info("Transaction cannot be created because it's not enough balance.");
            }
        }
    }

    private static Transaction createTransaction(TransactionRequestDTO transactionDTO) {
        // Keep in mind the exchange rate if there is one
        return Transaction.builder()
                .date(LocalDate.now())
                .transactionType(transactionDTO.getTransactionType())
                .productType(transactionDTO.getProductType())
                .productName(transactionDTO.getProductName())
                .singlePrice(transactionDTO.getSinglePrice())
                .quantity(transactionDTO.getQuantity())
                .exchangeRate(transactionDTO.getExchangeRate())
                .totalAmount(transactionDTO.getSinglePrice().multiply(transactionDTO.getQuantity()))
                .currency(transactionDTO.getCurrency())
                .build();
    }
}
