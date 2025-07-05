package com.investments.tracker.service;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.enums.Status.ACTIVE;

@Service
@Slf4j
public class PortfolioService{
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public void updatePortfolioForBuyTransaction(TransactionRequest transactionRequest, BigDecimal totalTransactionValue) {
        LocalDate transactionDate = transactionRequest.getDate();
        String productName = transactionRequest.getProductName();

        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            int newQuantity = portfolioForProduct.get().getQuantity() + transactionRequest.getQuantity();
            BigDecimal newInvestedMoney = portfolioForProduct.get().getInvestedMoney().add(totalTransactionValue);
            int updatedResult = this.portfolioRepository.updatePortfolioWithBuyTransaction(transactionDate, productName, newQuantity, newInvestedMoney);
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
            this.portfolioRepository.save(portfolio);
        }
    }



    public void updatePortfolioForSellTransaction() {

    }
}

