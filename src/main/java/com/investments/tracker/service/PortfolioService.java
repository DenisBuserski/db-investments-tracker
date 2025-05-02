package com.investments.tracker.service;

import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;

import java.math.BigDecimal;

public interface PortfolioService {
    void updatePortfolioWithTransaction(TransactionRequestDTO transactionRequestDTO, BigDecimal totalTransactionValue, BigDecimal totalDividendValue);
}
