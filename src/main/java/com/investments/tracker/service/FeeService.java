package com.investments.tracker.service;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.enums.FeeType;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.investments.tracker.enums.CashTransactionType.FEE;


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

    public BigDecimal getTotalAmountOfInsertedFees(TransactionRequest transactionRequest, long transactionId) {
        if (!transactionRequest.getFees().isEmpty()) {
            List<CashTransaction> fees = createFees(transactionRequest, transactionId);
            log.info("Total number of applied fees for transaction_id={} -> [{}]", transactionId, fees.size());
            return calculateTotalFees(fees);
        } else {
            log.info("No related fees for transaction_id={}", transactionId);
            return BigDecimal.ZERO;
        }
    }

    private List<CashTransaction> createFees(TransactionRequest transactionRequest, long transactionId) {
        Map<FeeType, BigDecimal> feesMap = transactionRequest.getFees();

        List<CashTransaction> fees = feesMap.entrySet()
                .stream()
                .map(entry -> {
                    String feeType = checkFeeType(entry.getKey());
                    BigDecimal feeValue = entry.getValue();
                    return this.cashTransactionService.createCashTransactionForFee(transactionRequest.getDate(), FEE, feeType, feeValue, transactionId);
                })
                .collect(Collectors.toList());
        return this.cashTransactionRepository.saveAll(fees);
    }

    private String checkFeeType(FeeType feeType) {
        String name = feeType.name();
        for (FeeType type : FeeType.values()) {
            if (type.name().equals(name)) {
                return type.getName();
            }
        }
        throw new IllegalArgumentException("Unknown fee type name: " + name);
    }

    private BigDecimal calculateTotalFees(List<CashTransaction> fees) {
        return fees.stream()
                .map(CashTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalFeesAmount() {
        return null;
    }

    public List<CashTransactionResponse> getAllFeesFromTo(LocalDate startDate, LocalDate now) {
        return null;
    }
}
