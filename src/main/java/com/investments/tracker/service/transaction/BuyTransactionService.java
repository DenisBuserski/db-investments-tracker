package com.investments.tracker.service.transaction;

import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.FeeService;
import com.investments.tracker.service.PortfolioService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.service.transaction.TransactionService.createTransaction;

@Service
@Slf4j
public class BuyTransactionService {
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final FeeService feeService;
    private final PortfolioService portfolioService;
    private final TransactionBalanceBuilderService balanceService;

    @Autowired
    public BuyTransactionService(
            BalanceRepository balanceRepository,
            TransactionRepository transactionRepository,
            FeeService feeService,
            PortfolioService portfolioService,
            TransactionBalanceBuilderService balanceService) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.feeService = feeService;
        this.portfolioService = portfolioService;
        this.balanceService = balanceService;
    }

    @Transactional
    public BalanceResponse insertBuyTransaction(Balance currentBalance, BigDecimal transactionValue, TransactionRequest transactionRequest) {
        log.info("Preparing [BUY] transaction with the following params: [CurrentBalance:{} | TransactionValue:{}]",
                    currentBalance.getBalance(), transactionValue);
        Transaction transaction = createTransaction(transactionRequest, transactionValue);
        transactionRepository.save(transaction);

        log.info("Start calculating fees");
        BigDecimal totalAmountOfInsertedFees = feeService.getTotalAmountOfInsertedFees(transactionRequest, transaction.getId());

        log.info("Start updating portfolio");
        portfolioService.updatePortfolioForBuyTransaction(transactionRequest, transactionValue);

            Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
            this.balanceRepository.save(newBalance);
            log.info("Successful [BUY] transaction for product [{}] on [{}]", transactionRequest.getProductName(), transactionRequest.getDate());

            return createBalanceResponse(newBalance);

    }
}
