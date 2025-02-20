package com.investments.tracker.service;

import com.investments.tracker.model.dto.TransactionRequestDTO;

public interface TransactionService {
    void insertTransaction(TransactionRequestDTO transactionDTO);
}
