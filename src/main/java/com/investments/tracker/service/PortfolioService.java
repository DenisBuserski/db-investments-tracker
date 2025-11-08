package com.investments.tracker.service;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.repository.PortfolioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.enums.Status.ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService{
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public void updatePortfolioForBuyTransaction(TransactionRequest transactionRequest, BigDecimal totalTransactionValue) {
        LocalDate transactionDate = transactionRequest.getDate();
        String productName = transactionRequest.getProductName();

        Optional<Portfolio> portfolioForProduct = portfolioRepository.findByProductName(productName);
        if (portfolioForProduct.isPresent()) {
            int newQuantity = portfolioForProduct.get().getQuantity() + transactionRequest.getQuantity();
            BigDecimal newInvestedMoney = portfolioForProduct.get().getInvestedMoney().add(totalTransactionValue);
            BigDecimal newAveragePrice = newInvestedMoney.divide(BigDecimal.valueOf(newQuantity), 4, RoundingMode.HALF_UP);
            int updatedResult = portfolioRepository.updatePortfolioWithBuyTransaction(transactionDate, productName, newQuantity, newInvestedMoney, newAveragePrice);

            if (updatedResult == 1) {
                log.info("Portfolio updated successfully for product [{}]", productName);
            } else {
                log.warn("Portfolio for product [{}] was not updated", productName);
            }
        } else {
            Portfolio portfolio = Portfolio.builder()
                    .lastUpdated(transactionDate)
                    .productName(productName)
                    .quantity(transactionRequest.getQuantity())
                    .investedMoney(totalTransactionValue)
                    .averagePrice(totalTransactionValue.divide(BigDecimal.valueOf(transactionRequest.getQuantity()), 4, RoundingMode.DOWN))
                    .dividendsAmount(BigDecimal.ZERO)
                    .status(ACTIVE)
                    .build();
            log.info("Inserted product [{}] in portfolio for the first time", productName);
            portfolioRepository.save(portfolio);
        }
    }

    @Transactional
    public void updatePortfolioForSellTransaction() {

    }

    @Transactional
    public void updatePortfolioForDividend(LocalDate date, String productName, BigDecimal amount) {
        int updatedResult = portfolioRepository.updatePortfolioForDividend(date, productName, amount);

        if (updatedResult == 1) {
            log.info("Portfolio updated successfully for product [{}]", productName);
        } else {
            log.warn("Portfolio for product [{}] was not updated", productName);
        }

    }

    public Optional<Portfolio> findByProductName(String productName) {
        return portfolioRepository.findByProductName(productName);
    }
}

