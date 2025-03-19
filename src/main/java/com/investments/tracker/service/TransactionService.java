package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;

public interface TransactionService {
    BalanceResponseDTO insertTransaction(TransactionRequestDTO transactionDTO);
}
