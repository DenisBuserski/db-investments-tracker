package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.model.enums.FeeType;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.investments.tracker.model.enums.Currency.EUR;


@Service
@Slf4j
public class FeeService {
    private final CashTransactionRepository cashTransactionRepository;

    @Autowired
    public FeeService(CashTransactionRepository cashTransactionRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
    }

    public BigDecimal getTotalAmountOfInsertedFees(TransactionRequestDTO transactionRequestDTO, long transactionId) {
        if (!transactionRequestDTO.getFees().isEmpty()) {
            List<CashTransaction> fees = createFees(transactionRequestDTO, transactionId);
            log.info("Total fees for TransactionId: [{}] are [{}]", transactionId, fees.size());
            return calculateTotalFees(fees);
        } else {
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

            CashTransaction fee = CashTransaction.builder()
                    .date(transactionRequestDTO.getDate())
                    .cashTransactionType(cashTransactionType)
                    .amount(feeValue)
                    .currency(EUR)
                    .description("Reference to 'transaction' table")
                    .referenceId(transactionId)
                    .build();
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
