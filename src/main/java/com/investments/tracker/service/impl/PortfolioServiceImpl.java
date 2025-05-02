package com.investments.tracker.service.impl;

import com.investments.tracker.model.Portfolio;
import com.investments.tracker.model.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.model.enums.Status;
import com.investments.tracker.model.enums.TransactionType;
import com.investments.tracker.repository.PortfolioRepository;
import com.investments.tracker.service.PortfolioService;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public void updatePortfolioWithTransaction(TransactionRequestDTO transactionRequestDTO, BigDecimal totalTransactionValue, BigDecimal totalDividendValue) {
        // Check if product exists
        LocalDate transactionDate = transactionRequestDTO.getDate();
        TransactionType transactionType = transactionRequestDTO.getTransactionType();
        String productName = transactionRequestDTO.getProductName();


        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
        if (!portfolioForProduct.isEmpty()) {
            if (transactionType.equals(BUY)) {

                Portfolio = Portfolio.builder()
                        .lastUpdated()
                        .productName()
                        .quantity()
                        .investedMoney()
                        .dividendsAmount()
                        .status()
                        .build();

            } else if (transactionType.equals(SELL)) {

            }

        } else {

        }





    }
}
