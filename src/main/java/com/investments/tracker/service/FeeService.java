package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.enums.FeeType;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FeeService {
    private final CashTransactionRepository cashTransactionRepository;
    private final CashTransactionService cashTransactionService;

    @Autowired
    public FeeService(CashTransactionRepository cashTransactionRepository,
                          CashTransactionService cashTransactionService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.cashTransactionService = cashTransactionService;
    }

    public BigDecimal getTotalAmountOfInsertedFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        if (!transactionRequestDTO.getFees().isEmpty()) {
            List<CashTransaction> fees = createFees(transactionRequestDTO, transactionId);
            log.info("Total number of applied fees for transaction_id={} -> [{}]", transactionId, fees.size());
            return calculateTotalFees(fees);
        } else {
            log.info("No related fees for transaction_id={}", transactionId);
            return BigDecimal.ZERO;
        }
    }

    private List<CashTransaction> createFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        Map<FeeType, BigDecimal> feesMap = transactionRequestDTO.getFees();

        List<CashTransaction> fees = feesMap.entrySet()
                .stream()
                .map(entry -> {
                    FeeType feeType = entry.getKey();
                    BigDecimal feeValue = entry.getValue();
                    CashTransactionType cashTransactionType = getCashtransactionType(feeType);
                    return this.cashTransactionService.createCashTransactionForFee(transactionRequestDTO.getDate(), cashTransactionType, feeValue, transactionId);
                })
                .collect(Collectors.toList());
        return this.cashTransactionRepository.saveAll(fees);
    }

    private static CashTransactionType getCashtransactionType(FeeType feeType) {
        if (feeType == null) {
            throw new NullPointerException("Fee type is NULL");
        }

        switch (feeType.name()) {
            case "TRANSACTION_EXECUTION_FEE":
                return CashTransactionType.TRANSACTION_EXECUTION_FEE;
            default:
                throw new IllegalArgumentException("Unknown FeeType: " + feeType);
        }
    }

    private BigDecimal calculateTotalFees(List<CashTransaction> fees) {
        return fees.stream()
                .map(CashTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
