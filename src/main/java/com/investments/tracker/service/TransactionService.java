package com.investments.tracker.service;

import com.investments.tracker.model.dto.TransactionDTO;
import jakarta.validation.Valid;

public interface TransactionService {
    void insertTransaction(TransactionDTO transactionDTO);
}
