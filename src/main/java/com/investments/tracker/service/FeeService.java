package com.investments.tracker.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.enums.FeeType;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.investments.tracker.enums.CashTransactionType.FEE;


@Service
@Slf4j
@RequiredArgsConstructor
public class FeeService {
    private final CashTransactionRepository cashTransactionRepository;
    private final CashTransactionService cashTransactionService;

    public BigDecimal calculateTotalAmountOfInsertedFees(TransactionRequest transactionRequest, long transactionId) {
        if (!transactionRequest.getFees().isEmpty()) {
            List<CashTransaction> fees = createFees(transactionRequest, transactionId);
            log.info("Total number of applied fees for transaction_id={} -> [{}]", transactionId, fees.size());
            return fees
                    .stream()
                    .map(CashTransaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            log.info("No related fees for transaction_id={}", transactionId);
            return BigDecimal.ZERO;
        }
    }

    private List<CashTransaction> createFees(TransactionRequest transactionRequest, long transactionId) {
        List<CashTransaction> fees  = transactionRequest
                .getFees()
                .entrySet()
                .stream()
                .map(entry -> {
                    String feeType = checkFeeType(entry.getKey());
                    BigDecimal feeValue = entry.getValue();
                    return cashTransactionService.createCashTransactionForFee(transactionRequest.getDate(), FEE, feeType, feeValue, transactionId);
                }).toList();
        return cashTransactionRepository.saveAll(fees);
    }

    private String checkFeeType(FeeType feeType) {
        if (feeType == null) {
            throw new IllegalArgumentException("Unknown fee type");
        }
        return feeType.getName();
    }

    // TODO: Implement
    public BigDecimal getTotalFeesAmount() {
        return null;
    }

    // TODO: Implement
    public List<CashTransactionResponse> getAllFeesFromTo(LocalDate startDate, LocalDate now) {
        return null;
    }
}
