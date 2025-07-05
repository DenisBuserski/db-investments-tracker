package com.investments.tracker.service.dividend;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Dividend;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.DividendRequest;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.repository.DividendRepository;
import com.investments.tracker.service.CashTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static java.math.RoundingMode.CEILING;

@Service
@Slf4j
public class DividendService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final DividendRepository dividendRepository;
    private final CashTransactionService cashtransactionService;
    private final DividendBalanceBuilderService dividendBalanceBuilderService;

    @Autowired
    public DividendService(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            DividendRepository dividendRepository,
            CashTransactionService cashtransactionService,
            DividendBalanceBuilderService dividendBalanceBuilderService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.dividendRepository = dividendRepository;
        this.cashtransactionService = cashtransactionService;
        this.dividendBalanceBuilderService = dividendBalanceBuilderService;
    }

    // TODO: Check the scaling with the exchange rate
    public BalanceResponse insertDividend(DividendRequest dividendRequest) {
        BigDecimal exchangeRate = dividendRequest.getExchangeRate() == null ? BigDecimal.ZERO : dividendRequest.getExchangeRate();
        BigDecimal dividendAmountBeforeConversion = calculateDividendAmount(dividendRequest);
        BigDecimal dividendAmountAfterConversion = dividendConversion(exchangeRate, dividendAmountBeforeConversion);

        CashTransaction dividend = this.cashtransactionService.createCashTransactionForDividend(dividendRequest, dividendAmountAfterConversion);
        this.cashTransactionRepository.save(dividend);

        Dividend dividendEntity = creteDividend(dividendRequest);
        this.dividendRepository.save(dividendEntity);

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        Balance newBalance;
        if (latestBalance.isPresent()) {
            newBalance = this.dividendBalanceBuilderService.createBalanceFromCashTransaction(latestBalance.get(), dividend);
        } else {
            newBalance = this.dividendBalanceBuilderService.createBalanceFromCashTransaction(null, dividend);
        }

        this.balanceRepository.save(newBalance);
        log.info("Dividend for product [{}] created successfully", dividendRequest.getProductName());
        return createBalanceResponse(newBalance);
    }

    private static BigDecimal calculateDividendAmount(DividendRequest dividendRequest) {
        BigDecimal dividendAmountBeforeConversion = dividendRequest.getDividendAmount();
        BigDecimal dividendTaxBeforeConversion = dividendRequest.getDividendTax();
        return dividendAmountBeforeConversion.subtract(dividendTaxBeforeConversion);
    }

    private static BigDecimal dividendConversion(BigDecimal exchangeRate, BigDecimal dividendAmountBeforeConversion) {
        if (!exchangeRate.equals(BigDecimal.ZERO)) {
            return dividendAmountBeforeConversion.divide(exchangeRate, 10, CEILING).setScale(2, CEILING);
        } else {
            return dividendAmountBeforeConversion;
        }
    }

    private static Dividend creteDividend(DividendRequest dividendRequest) {
        int quantity = dividendRequest.getQuantity();
        BigDecimal dividendAmount = dividendRequest.getDividendAmount();
        BigDecimal dividendTaxAmount = dividendRequest.getDividendTax();
        BigDecimal dividendAmountPerShare = dividendAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);
        BigDecimal dividendTaxAmountPerShare = dividendTaxAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);

        return Dividend.builder()
                .date(dividendRequest.getDate())
                .productName(dividendRequest.getProductName())
                .quantity(quantity)
                .dividendAmount(dividendAmount)
                .dividendTaxAmount(dividendTaxAmount)
                .dividendAmountPerShare(dividendAmountPerShare)
                .dividendTaxAmountPerShare(dividendTaxAmountPerShare)
                .exchangeRate(dividendRequest.getExchangeRate())
                .currency(dividendRequest.getCurrency())
                .build();

    }


    public BigDecimal getTotalDividendsAmount() {
        return null;
    }

    public List<CashTransactionResponse> getAllDividendsFromTo(LocalDate startDate, LocalDate now) {
        return null;
    }
}
