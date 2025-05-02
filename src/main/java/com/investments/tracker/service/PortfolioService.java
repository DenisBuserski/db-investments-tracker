package com.investments.tracker.service;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.Status;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.model.enums.Status.ACTIVE;
import static com.investments.tracker.model.enums.TransactionType.BUY;
import static com.investments.tracker.model.enums.TransactionType.SELL;

@Service
@Slf4j
public class PortfolioService{
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public void updatePortfolioWithBuyTransaction(TransactionRequestDTO transactionRequestDTO, BigDecimal totalTransactionValue) {
        // Check if product exists
        LocalDate transactionDate = transactionRequestDTO.getDate();
        String productName = transactionRequestDTO.getProductName();

        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            // Insert product transaction for existing product
            int newQuantity = portfolioForProduct.get().getQuantity() + transactionRequestDTO.getQuantity();
            BigDecimal newInvestedMoney = portfolioForProduct.get().getInvestedMoney().add(totalTransactionValue);
            int updatedResult = this.portfolioRepository.updatePortfolio(transactionDate, productName, newQuantity, newInvestedMoney);
            if (updatedResult == 1) {
                log.info("Portfolio updated successfully");
            } else {
                log.warn("Portfolio was not updated");
            }
        } else {
            // Insert product transaction for the first time
            Portfolio portfolio = Portfolio.builder()
                    .lastUpdated(transactionDate)
                    .productName(productName)
                    .quantity(transactionRequestDTO.getQuantity())
                    .investedMoney(totalTransactionValue)
                    .dividendsAmount(BigDecimal.ZERO)
                    .status(ACTIVE)
                    .build();
            this.portfolioRepository.save(portfolio);
        }





    }
}

