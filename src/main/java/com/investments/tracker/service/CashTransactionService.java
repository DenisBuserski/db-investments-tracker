package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.dividend.DividendRequestDTO;
import com.investments.tracker.enums.CashTransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.enums.Currency.EUR;

@Service
public class CashTransactionService {
    public CashTransaction createCashTransactionForDividend(DividendRequestDTO dividendRequestDTO, BigDecimal dividendAmount) {
        String dividendDescription = String.format("Dividend for product [%s]", dividendRequestDTO.getProductName());
        return CashTransaction.builder()
                .date(dividendRequestDTO.getDate())
                .cashTransactionType(DIVIDEND)
                .amount(dividendAmount)
                .currency(EUR)
                .description(dividendDescription)
                .referenceId(null)
                .build();
    }

    public CashTransaction createCashTransactionForFee(LocalDate date, CashTransactionType cashTransactionType, BigDecimal feeValue, long transactionId) {
        return CashTransaction.builder()
                .date(date)
                .cashTransactionType(cashTransactionType)
                .amount(feeValue)
                .currency(EUR)
                .description("Reference to 'transaction' table")
                .referenceId(transactionId)
                .build();
    }

}
