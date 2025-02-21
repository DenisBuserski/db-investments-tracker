package com.investments.tracker.service.impl;

import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.TransactionRequestDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        // Create Transaction
        Transaction transaction = createTransaction(transactionDTO);
        this.transactionRepository.save(transaction);
        // Take money from the Balance


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
