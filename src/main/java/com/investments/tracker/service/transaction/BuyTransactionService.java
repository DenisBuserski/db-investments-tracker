package com.investments.tracker.service.transaction;

import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.FeeService;
import com.investments.tracker.service.PortfolioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.investments.tracker.controller.balance.BalanceResponse.createBalanceResponse;


@Service
@Slf4j
@RequiredArgsConstructor
public class BuyTransactionService {
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final FeeService feeService;
    private final PortfolioService portfolioService;
    private final TransactionBalanceBuilderService balanceService;
    private final TransactionBuilder transactionBuilder;

    @Transactional
    public BalanceResponse insertBuyTransaction(Balance currentBalance, BigDecimal transactionValue, TransactionRequest transactionRequest) {
        log.info("Preparing [BUY] transaction with the following params: [CurrentBalance:{} | TransactionValue:{}]", currentBalance.getBalance(), transactionValue);
        Transaction transaction = transactionBuilder.createBuyTransaction(transactionRequest, transactionValue);
        transactionRepository.save(transaction);

        log.info("Start calculating fees");
        BigDecimal totalAmountOfInsertedFees = feeService.calculateTotalAmountOfInsertedFees(transactionRequest, transaction.getId());

        log.info("Start updating portfolio");
        portfolioService.updatePortfolioForBuyTransaction(transactionRequest, transactionValue);

        Balance newBalance = balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
        balanceRepository.save(newBalance);
        log.info("Successful [BUY] transaction for product: {} on date {}", transactionRequest.getProductName(), transactionRequest.getDate());
        return createBalanceResponse(newBalance);
    }
}
