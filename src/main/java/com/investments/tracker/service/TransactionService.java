package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static com.investments.tracker.model.enums.Currency.EUR;


@Service
@Slf4j
public class TransactionService{
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final FeeService feeService;
    private final BalanceService balanceService;
    private final PortfolioService portfolioService;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            BalanceRepository balanceRepository,
            FeeService feeService,
            BalanceService balanceService,
            PortfolioService portfolioService) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
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
            BigDecimal balanceValue = currentBalance.get().getBalance();
            BigDecimal transactionValue = calculateTransactionValue(transactionRequestDTO);

            if (balanceValue.compareTo(transactionValue) >= 0) {
                Transaction transaction = createTransaction(transactionRequestDTO, transactionValue);
                this.transactionRepository.save(transaction);


                BigDecimal totalAmountOfInsertedFees = this.feeService.getTotalAmountOfInsertedFees(transactionRequestDTO, transaction.getId());

                // Update or create portfolio
                this.portfolioService.updatePortfolioWithTransaction(transactionRequestDTO, transactionValue, BigDecimal.ZERO);

                Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance.get(), transaction, totalAmountOfInsertedFees);
                this.balanceRepository.save(newBalance);
                log.info("Successful [{}] transaction for date [{}] and product [{}]", transaction.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());

                return createBalanceResponseDTO(newBalance);
            } else {
                log.info("Transaction cannot be created because there is not enough balance.");
                return createBalanceResponseDTO(null);
            }
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
