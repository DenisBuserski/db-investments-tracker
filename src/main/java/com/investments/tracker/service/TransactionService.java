package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.PortfolioRepository;
import com.investments.tracker.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static com.investments.tracker.model.enums.Currency.EUR;
import static com.investments.tracker.model.enums.Status.ACTIVE;
import static com.investments.tracker.model.enums.TransactionType.*;
import static com.investments.tracker.model.enums.TransactionType.BUY;


@Service
@Slf4j
public class TransactionService{
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final PortfolioRepository portfolioRepository;
    private final FeeService feeService;
    private final BalanceService balanceService;
    private final PortfolioService portfolioService;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            BalanceRepository balanceRepository,
            PortfolioRepository portfolioRepository,
            FeeService feeService,
            BalanceService balanceService,
            PortfolioService portfolioService) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
        this.portfolioRepository = portfolioRepository;
        this.feeService = feeService;
        this.balanceService = balanceService;
        this.portfolioService = portfolioService;
    }

    public BalanceResponseDTO insertTransaction(TransactionRequestDTO transactionRequestDTO) {
        Optional<Balance> currentBalance = this.balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            log.error("Transaction cannot be created because there is no current balance!");
            return createBalanceResponseDTO(null);
        } else {
            TransactionType transactionType = transactionRequestDTO.getTransactionType();
            BigDecimal balanceValue = currentBalance.get().getBalance();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequestDTO);

            BalanceResponseDTO balanceResponseDTO = null;
            if (transactionType == BUY) {
                balanceResponseDTO = buyTransaction(currentBalance.get(), balanceValue, transactionValue, transactionRequestDTO);
            } else if (transactionType == SELL) {
                balanceResponseDTO = sellTransaction(currentBalance.get(), transactionValue, transactionRequestDTO);
            }
            return balanceResponseDTO;
        }
    }

    private BalanceResponseDTO buyTransaction(Balance currentBalance, BigDecimal balanceValue, BigDecimal transactionValue, TransactionRequestDTO transactionRequestDTO) {
        if (balanceValue.compareTo(transactionValue) >= 0) {
            Transaction transaction = createTransaction(transactionRequestDTO, transactionValue);
            this.transactionRepository.save(transaction);

            BigDecimal totalAmountOfInsertedFees = this.feeService.getTotalAmountOfInsertedFees(transactionRequestDTO, transaction.getId());

            this.portfolioService.updatePortfolioWithBuyTransaction(transactionRequestDTO, transactionValue);

            Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
            this.balanceRepository.save(newBalance);
            log.info("Successful [{}] transaction for date [{}] and product [{}]", transaction.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());

            return createBalanceResponseDTO(newBalance);
        } else {
            log.info("Transaction cannot be created because there is not enough balance.");
            return createBalanceResponseDTO(null);
        }
    }

    // TODO: Checking selling
    private BalanceResponseDTO sellTransaction(Balance currentBalance, BigDecimal transactionValue, TransactionRequestDTO transactionRequestDTO) {
        String productName = transactionRequestDTO.getProductName();
        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            Portfolio portfolio = portfolioForProduct.get();
            int currentQuantity = portfolio.getQuantity();
            if (currentQuantity >= transactionRequestDTO.getQuantity()) {
                Transaction transaction = createTransaction(transactionRequestDTO, transactionValue);
                this.transactionRepository.save(transaction);

                BigDecimal totalAmountOfInsertedFees = this.feeService.getTotalAmountOfInsertedFees(transactionRequestDTO, transaction.getId());

                this.portfolioService.updatePortfolioWithSellTransaction(transactionRequestDTO, transactionValue);

                Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
                this.balanceRepository.save(newBalance);
                log.info("Successful [{}] transaction for date [{}] and product [{}]", transaction.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());

                return createBalanceResponseDTO(newBalance);
            } else {
                log.info("Transaction cannot be created because there is not enough quantity of product [{}]", productName);
                return createBalanceResponseDTO(null);
            }
        } else {
            log.info("Transaction cannot be created because product [{}] does not exist in the portfolio", productName);
            return createBalanceResponseDTO(null);
        }
    }

    private static BigDecimal calculateTransactionValue(TransactionRequestDTO transactionRequestDTO) {
        BigDecimal exchangeRate = transactionRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequestDTO.getExchangeRate();
        BigDecimal singlePrice = transactionRequestDTO.getSinglePrice();
        int quantity = transactionRequestDTO.getQuantity();
        BigDecimal calculationWithoutExchangeRate = singlePrice.multiply(BigDecimal.valueOf(quantity));

        if (exchangeRate.equals(BigDecimal.ZERO)) {
            return calculationWithoutExchangeRate;
        } else {
            return calculationWithoutExchangeRate.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private static Transaction createTransaction(TransactionRequestDTO transactionRequestDTO, BigDecimal transactionValue) {
        BigDecimal exchangeRate = transactionRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : transactionRequestDTO.getExchangeRate();
        String description = transactionRequestDTO.getFees().isEmpty() ? "No fees related to this transaction" : "Check 'cashtransaction' table for related fees";
        return Transaction.builder()
                .date(transactionRequestDTO.getDate())
                .transactionType(transactionRequestDTO.getTransactionType())
                .productType(transactionRequestDTO.getProductType())
                .productName(transactionRequestDTO.getProductName())
                .singlePrice(transactionRequestDTO.getSinglePrice())
                .quantity(transactionRequestDTO.getQuantity())
                .exchangeRate(exchangeRate)
                .totalAmount(transactionValue)
                .currency(EUR)
                .baseCurrency(transactionRequestDTO.getCurrency())
                .description(description)
                .build();
    }

}
