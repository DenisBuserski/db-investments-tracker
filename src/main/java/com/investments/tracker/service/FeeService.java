package com.investments.tracker.service;

import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;

import java.math.BigDecimal;

public interface FeeService {
    BigDecimal getTotalAmountOfInsertedFees(TransactionRequestDTO transactionRequestDTO, long id);
}
