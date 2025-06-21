package com.investments.tracker.service;

import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponseDTO;
import static com.investments.tracker.service.TransactionService.createTransaction;

@Service
@Slf4j
public class BuyTransactionService {
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final FeeService feeService;
    private final PortfolioService portfolioService;
    private final BalanceService balanceService;

    @Autowired
    public BuyTransactionService(
            BalanceRepository balanceRepository,
            TransactionRepository transactionRepository,
            FeeService feeService,
            PortfolioService portfolioService,
            BalanceService balanceService) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.feeService = feeService;
        this.portfolioService = portfolioService;
        this.balanceService = balanceService;
    }

    public BalanceResponse insertBuyTransaction(Balance currentBalance, BigDecimal balanceValue, BigDecimal transactionValue, TransactionRequest transactionRequest) {
        if (balanceValue.compareTo(transactionValue) >= 0) {
            Transaction transaction = createTransaction(transactionRequest, transactionValue);
            this.transactionRepository.save(transaction);

            BigDecimal totalAmountOfInsertedFees = this.feeService.getTotalAmountOfInsertedFees(transactionRequest, transaction.getId());

            this.portfolioService.updatePortfolioForBuyTransaction(transactionRequest, transactionValue);

            Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
            this.balanceRepository.save(newBalance);
            log.info("Successful [{}] transaction for product [{}] on [{}]", transaction.getTransactionType(), transactionRequest.getProductName(), transactionRequest.getDate());

            return createBalanceResponseDTO(newBalance);
        } else {
            log.info("Transaction cannot be created because there is not enough balance.");
            return createBalanceResponseDTO(null);
        }
    }
}
