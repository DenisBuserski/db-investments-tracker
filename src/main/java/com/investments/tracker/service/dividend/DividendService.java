package com.investments.tracker.service.dividend;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.enums.Status;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Dividend;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.dividend.DividendRequest;
import com.investments.tracker.model.Portfolio;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.repository.DividendRepository;
import com.investments.tracker.service.CashTransactionService;
import com.investments.tracker.service.PortfolioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.balance.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.validation.ValidationMessages.PRODUCT_NOT_EXIST;
import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.FLOOR;

@Service
@Slf4j
@RequiredArgsConstructor
public class DividendService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final DividendRepository dividendRepository;
    private final CashTransactionService cashtransactionService;
    private final DividendBalanceBuilderService dividendBalanceBuilderService;
    private final PortfolioService portfolioService;

    @Transactional
    public BalanceResponse insertDividend(DividendRequest dividendRequest) {
        Optional<Portfolio> portfolioForProduct = portfolioService.findByProductName(dividendRequest.getProductName());

        if (portfolioForProduct.isPresent() && portfolioForProduct.get().getStatus().equals(Status.ACTIVE)) {
            BigDecimal exchangeRate = dividendRequest.getExchangeRate() == null ? BigDecimal.ZERO : dividendRequest.getExchangeRate();
            BigDecimal dividendAmountReceivedBeforeConversion = calculateDividendAmountReceived(dividendRequest);
            BigDecimal dividendAmountReceivedAfterConversion = dividendConversion(exchangeRate, dividendAmountReceivedBeforeConversion);

            CashTransaction dividend = cashtransactionService.createCashTransactionForDividend(dividendRequest, dividendAmountReceivedAfterConversion);
            cashTransactionRepository.save(dividend);

            Dividend dividendEntity = creteDividendEntity(dividendRequest, dividendAmountReceivedBeforeConversion, dividendAmountReceivedAfterConversion);
            dividendRepository.save(dividendEntity);

            portfolioService.updatePortfolioForDividend(dividend.getDate(), dividendRequest.getProductName(), dividend.getAmount());

            Optional<Balance> latestBalance = balanceRepository.getLatestBalance();
            Balance newBalance = dividendBalanceBuilderService.createBalanceFromCashTransaction(latestBalance.get(), dividend);
            balanceRepository.save(newBalance);
            log.info("Dividend for product: {} created successfully", dividendRequest.getProductName());
            return createBalanceResponse(newBalance);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PRODUCT_NOT_EXIST.replace("%1", dividendRequest.getProductName()));
        }
    }

    private static BigDecimal calculateDividendAmountReceived(DividendRequest dividendRequest) {
        BigDecimal dividendAmountBeforeConversion = dividendRequest.getTotalDividendReceived();
        BigDecimal dividendTaxBeforeConversion = dividendRequest.getTotalDividendTaxCharged();
        return dividendAmountBeforeConversion.subtract(dividendTaxBeforeConversion);
    }

    private static BigDecimal dividendConversion(BigDecimal exchangeRate, BigDecimal dividendAmountBeforeConversion) {
        if (!exchangeRate.equals(BigDecimal.ZERO)) {
            return dividendAmountBeforeConversion.divide(exchangeRate, 2, RoundingMode.HALF_UP);
        } else {
            return dividendAmountBeforeConversion;
        }
    }

    private static Dividend creteDividendEntity(
            DividendRequest dividendRequest,
            BigDecimal dividendAmountReceivedBeforeConversion,
            BigDecimal dividendAmountReceivedAfterConversion) {
        int quantity = dividendRequest.getQuantity();
        BigDecimal dividendAmount = dividendRequest.getTotalDividendReceived();
        BigDecimal dividendTaxAmount = dividendRequest.getTotalDividendTaxCharged();
        BigDecimal dividendAmountPerShare = dividendAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);
        BigDecimal dividendTaxAmountPerShare = dividendTaxAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);

        return Dividend.builder()
                .date(dividendRequest.getDate())
                .productName(dividendRequest.getProductName())
                .quantity(quantity)
                .totalAmountAfterTaxAndConversion(dividendAmountReceivedAfterConversion)
                .totalAmountAfterTaxBeforeConversion(dividendAmountReceivedBeforeConversion)
                .totalBaseAmount(dividendAmount)
                .totalBaseTaxAmount(dividendTaxAmount)
                .amountPerShare(dividendAmountPerShare)
                .taxAmountPerShare(dividendTaxAmountPerShare)
                .exchangeRate(dividendRequest.getExchangeRate())
                .dividendCurrency(dividendRequest.getCurrency())
                .build();

    }


    public BigDecimal getTotalDividendsAmount() {
        return null;
    }

    public List<CashTransactionResponse> getAllDividendsFromTo(LocalDate startDate, LocalDate now) {
        return null;
    }
}
