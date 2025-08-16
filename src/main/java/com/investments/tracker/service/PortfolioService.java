package com.investments.tracker.service;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.controller.transaction.TransactionRequest;
import com.investments.tracker.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.enums.Status.ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioService{
    private final PortfolioRepository portfolioRepository;

    // Add average price
    public void updatePortfolioForBuyTransaction(TransactionRequest transactionRequest, BigDecimal totalTransactionValue) {
        LocalDate transactionDate = transactionRequest.getDate();
        String productName = transactionRequest.getProductName();

        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            int newQuantity = portfolioForProduct.get().getQuantity() + transactionRequest.getQuantity();
            BigDecimal newInvestedMoney = portfolioForProduct.get().getInvestedMoney().add(totalTransactionValue);
            int updatedResult = portfolioRepository.updatePortfolioWithBuyTransaction(transactionDate, productName, newQuantity, newInvestedMoney);
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
                    .dividendsAmount(BigDecimal.ZERO)
                    .status(ACTIVE)
                    .build();
            log.info("Inserted product [{}] in portfolio for the first time", productName);
            portfolioRepository.save(portfolio);
        }
    }



    public void updatePortfolioForSellTransaction() {

    }
}

