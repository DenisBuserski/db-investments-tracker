package com.investments.tracker.service;

import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.investments.tracker.model.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.model.enums.Currency.EUR;

@Service
public class CashTransactionService {
    public static CashTransaction createCashtransaction(DividendRequestDTO dividendRequestDTO, BigDecimal dividendAmount) {
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
}
