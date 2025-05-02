package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Dividend;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.repository.DividendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static java.math.RoundingMode.CEILING;

@Service
@Slf4j
public class DividendService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final DividendRepository dividendRepository;
    private final CashTransactionService cashtransactionService;
    private final BalanceService balanceService;

    @Autowired
    public DividendService(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            DividendRepository dividendRepository,
            CashTransactionService cashtransactionService,
            BalanceService balanceService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.dividendRepository = dividendRepository;
        this.cashtransactionService = cashtransactionService;
        this.balanceService = balanceService;
    }

    // TODO: Check the scaling with the exchange rate
    public BalanceResponseDTO insertDividend(DividendRequestDTO dividendRequestDTO) {
        BigDecimal exchangeRate = dividendRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : dividendRequestDTO.getExchangeRate();
        BigDecimal dividendAmountBeforeConversion = calculateDividendAmount(dividendRequestDTO);
        BigDecimal dividendAmountAfterConversion = dividendConversion(exchangeRate, dividendAmountBeforeConversion);

        CashTransaction dividend = this.cashtransactionService.createCashTransactionForDividend(dividendRequestDTO, dividendAmountAfterConversion);
        this.cashTransactionRepository.save(dividend);

        Dividend dividendEntity = creteDividend(dividendRequestDTO);
        this.dividendRepository.save(dividendEntity);

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        Balance newBalance;
        if (latestBalance.isPresent()) {
            newBalance = this.balanceService.createNewBalanceFromDividend(latestBalance.get(), dividend);
        } else {
            newBalance = this.balanceService.createNewBalanceFromDividend(null, dividend);
        }

        this.balanceRepository.save(newBalance);
        log.info("Dividend for product [{}] created successfully", dividendRequestDTO.getProductName());
        return createBalanceResponseDTO(newBalance);
    }

    private static BigDecimal calculateDividendAmount(DividendRequestDTO dividendRequestDTO) {
        BigDecimal dividendAmountBeforeConversion = dividendRequestDTO.getDividendAmount();
        BigDecimal dividendTaxBeforeConversion = dividendRequestDTO.getDividendTax();
        return dividendAmountBeforeConversion.subtract(dividendTaxBeforeConversion);
    }

    private static BigDecimal dividendConversion(BigDecimal exchangeRate, BigDecimal dividendAmountBeforeConversion) {
        if (!exchangeRate.equals(BigDecimal.ZERO)) {
            return dividendAmountBeforeConversion.divide(exchangeRate, 10, CEILING).setScale(2, CEILING);
        } else {
            return dividendAmountBeforeConversion;
        }
    }

    private static Dividend creteDividend(DividendRequestDTO dividendRequestDTO) {
        int quantity = dividendRequestDTO.getQuantity();
        BigDecimal dividendAmount = dividendRequestDTO.getDividendAmount();
        BigDecimal dividendTaxAmount = dividendRequestDTO.getDividendTax();
        BigDecimal dividendAmountPerShare = dividendAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);
        BigDecimal dividendTaxAmountPerShare = dividendTaxAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);

        return Dividend.builder()
                .date(dividendRequestDTO.getDate())
                .productName(dividendRequestDTO.getProductName())
                .quantity(quantity)
                .dividendAmount(dividendAmount)
                .dividendTaxAmount(dividendTaxAmount)
                .dividendAmountPerShare(dividendAmountPerShare)
                .dividendTaxAmountPerShare(dividendTaxAmountPerShare)
                .exchangeRate(dividendRequestDTO.getExchangeRate())
                .currency(dividendRequestDTO.getCurrency())
                .build();

    }


}
