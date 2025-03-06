package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.TransactionRequestDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            BalanceRepository balanceRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public BalanceResponseDTO insertTransaction(TransactionRequestDTO transactionDTO) {
        Optional<Balance> currentBalance = this.balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            log.error("There is no current balance. Transaction cannot be created.");
            return createBalanceResponseDTO(null);
        } else {
            BigDecimal balanceValue = currentBalance.get().getBalance();

            if (transactionDTO.getExchangeRate().equals(BigDecimal.ZERO)) {
                BigDecimal transactionValue = transactionDTO.getSinglePrice().multiply(BigDecimal.valueOf(transactionDTO.getQuantity()));
                if (balanceValue.compareTo(transactionValue) >= 0) {
                    Transaction transaction = createTransaction(transactionDTO);
                    this.transactionRepository.save(transaction);
                    log.info("Creating transaction for [{}] in table [{}]", transaction.getProductName(), "transactions");

                    Balance newBalance = createNewBalance(currentBalance.get(), transaction);
                    this.balanceRepository.save(newBalance);

                    // Update portfolio

                    return createBalanceResponseDTO(newBalance);
                } else {
                    log.info("Transaction cannot be created because it's not enough balance.");
                    return createBalanceResponseDTO(null);
                }
            } else {
                // Use exchange rate

                return createBalanceResponseDTO(null);
            }
        }
    }

    private static Transaction createTransaction(TransactionRequestDTO transactionDTO) {
        return Transaction.builder()
                .date(transactionDTO.getDate())
                .transactionType(transactionDTO.getTransactionType())
                .productType(transactionDTO.getProductType())
                .productName(transactionDTO.getProductName())
                .singlePrice(transactionDTO.getSinglePrice())
                .quantity(transactionDTO.getQuantity())
                .exchangeRate(transactionDTO.getExchangeRate()) // Keep in mind the exchange rate if there is one
                .totalAmount(transactionDTO.getSinglePrice().multiply(BigDecimal.valueOf(transactionDTO.getQuantity())))
                .currency(transactionDTO.getCurrency())
                .build();
    }

    private static Balance createNewBalance(Balance balance, Transaction transaction) {
        BigDecimal newBalanceValue = balance.getBalance().subtract(transaction.getTotalAmount());
        BigDecimal newTotalInvestments = balance.getTotalInvestments().add(transaction.getTotalAmount());

        return Balance.builder()
                .date(transaction.getDate())
                .balance(newBalanceValue)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(balance.getTotalDeposits())
                .totalWithdrawals(balance.getTotalWithdrawals())
                .totalDividends(balance.getTotalDividends())
                .totalFees(balance.getTotalFees())
                .lastPortfolioValue(balance.getLastPortfolioValue())
                .build();
    }

    private static BalanceResponseDTO createBalanceResponseDTO(Balance newBalance) {
        return BalanceResponseDTO.builder()
                .date(newBalance.getDate())
                .balance(newBalance.getBalance())
                .totalInvestments(newBalance.getTotalInvestments())
                .totalDeposits(newBalance.getTotalDeposits())
                .totalWithdrawals(newBalance.getTotalWithdrawals())
                .totalDividends(newBalance.getTotalDividends())
                .totalFees(newBalance.getTotalFees())
                .lastPortfolioValue(newBalance.getLastPortfolioValue())
                .build();
    }
}
