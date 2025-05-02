package com.investments.tracker.service.impl;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.enums.FeeType;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.CashTransactionService;
import com.investments.tracker.service.FeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class FeeServiceImpl implements FeeService {
    private final CashTransactionRepository cashTransactionRepository;
    private final CashTransactionService cashTransactionService;

    @Autowired
    public FeeServiceImpl(CashTransactionRepository cashTransactionRepository,
                          CashTransactionService cashTransactionService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.cashTransactionService = cashTransactionService;
    }

    @Override
    public BigDecimal getTotalAmountOfInsertedFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        if (!transactionRequestDTO.getFees().isEmpty()) {
            List<CashTransaction> fees = createFees(transactionRequestDTO, transactionId);
            log.info("Total fees for TransactionId={} are [{}]", transactionId, fees.size());
            return calculateTotalFees(fees);
        } else {
            log.info("No related fees for TransactionId={}", transactionId);
            return BigDecimal.ZERO;
        }
    }

    private List<CashTransaction> createFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        List<CashTransaction> fees = new ArrayList<>();
        Map<FeeType, BigDecimal> feesMap = transactionRequestDTO.getFees();

        for (Map.Entry<FeeType, BigDecimal> feeEntry : feesMap.entrySet()) {
            FeeType feeType = feeEntry.getKey();
            BigDecimal feeValue = feeEntry.getValue();

            CashTransactionType cashTransactionType = getCashtransactionType(feeType);

            CashTransaction fee = this.cashTransactionService.createCashTransactionForFee(transactionRequestDTO.getDate(), cashTransactionType, feeValue, transactionId);
            fees.add(fee);
        }
        this.cashTransactionRepository.saveAll(fees);
        return fees;
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
