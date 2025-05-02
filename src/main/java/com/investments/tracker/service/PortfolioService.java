package com.investments.tracker.service;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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

    public void updatePortfolioWithTransaction(TransactionRequestDTO transactionRequestDTO, BigDecimal totalTransactionValue, BigDecimal totalDividendValue) {
        // Check if product exists
        LocalDate transactionDate = transactionRequestDTO.getDate();
        TransactionType transactionType = transactionRequestDTO.getTransactionType();
        String productName = transactionRequestDTO.getProductName();


        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            if (transactionType.equals(BUY)) {

//                Portfolio = Portfolio.builder()
//                        .lastUpdated()
//                        .productName()
//                        .quantity()
//                        .investedMoney()
//                        .dividendsAmount()
//                        .status()
//                        .build();

            } else if (transactionType.equals(SELL)) {

            }

        } else {

        }





    }
}
