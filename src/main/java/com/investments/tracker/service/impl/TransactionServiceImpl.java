package com.investments.tracker.service.impl;

import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.TransactionDTO;
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

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void insertTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
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
        this.transactionRepository.save(transaction);

        // TODO: Take money from the Balance

    }
}
