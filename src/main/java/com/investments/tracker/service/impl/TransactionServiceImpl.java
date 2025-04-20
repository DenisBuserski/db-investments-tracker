package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.enums.FeeType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.repository.PortfolioRepository;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionRepository cashTransactionRepository;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            BalanceRepository balanceRepository,
            CashTransactionRepository cashTransactionRepository,
            PortfolioRepository portfolioRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceRepository = balanceRepository;
        this.cashTransactionRepository = cashTransactionRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public BalanceResponseDTO insertTransaction(TransactionRequestDTO transactionRequestDTO) {
        Optional<Balance> currentBalance = this.balanceRepository.getLatestBalance();
        if (!currentBalance.isPresent()) {
            log.error("There is no current balance. Transaction cannot be created.");
            return createBalanceResponseDTO(null);
        } else {
            BigDecimal balanceValue = currentBalance.get().getBalance();

            BigDecimal transactionValue = calculateTransactionValue(transactionRequestDTO);

            if (balanceValue.compareTo(transactionValue) >= 0) {
                Transaction transaction = createTransaction(transactionRequestDTO, transactionValue);
                this.transactionRepository.save(transaction);
                log.info("Creating [{}] transaction for date [{}] for product [{}]", transaction.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());

                // Insert fees in cashtransaction table and connected them with the transaction id
                if (!transactionRequestDTO.getFees().isEmpty()) {
                    List<CashTransaction> fees = createFees(transactionRequestDTO, transaction.getId());
                    this.cashTransactionRepository.saveAll(fees);
                }


                Balance newBalance = createNewBalance(currentBalance.get(), transaction);
                this.balanceRepository.save(newBalance);

                // Update or create portfolio

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

    private static List<CashTransaction> createFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        List<CashTransaction> fees = new ArrayList<>();
        Map<FeeType, BigDecimal> feesMap = transactionRequestDTO.getFees();

        for (Map.Entry<FeeType, BigDecimal> feeEntry : feesMap.entrySet()) {
            FeeType feeType = feeEntry.getKey();
            BigDecimal feeValue = feeEntry.getValue();

            CashTransactionType cashTransactionType = getCashtransactionType(feeType);

            CashTransaction fee = CashTransaction.builder()
                    .date(transactionRequestDTO.getDate())
                    .cashTransactionType(cashTransactionType)
                    .amount(feeValue)
                    .currency(transactionRequestDTO.getCurrency())
                    .description("Reference to 'transaction' table")
                    .referenceId(transactionId)
                    .build();
            fees.add(fee);
        }
        return fees;
    }

    private static CashTransactionType getCashtransactionType(FeeType feeType) {

        return null;
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
                .currency(transactionRequestDTO.getCurrency())
                .description(description)
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
                .lastPortfolioValue(balance.getLastPortfolioValue()) // Check this
                .build();
    }

    private static BalanceResponseDTO createBalanceResponseDTO(Balance balance) {
        LocalDate newDate = balance == null ? LocalDate.now(): balance.getDate();
        BigDecimal newBalanceAmount = balance == null ? BigDecimal.ZERO : balance.getBalance();
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? BigDecimal.ZERO : balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? BigDecimal.ZERO : balance.getTotalDividends();
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();

        return BalanceResponseDTO.builder()
                .date(newDate)
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .build();
    }
}
