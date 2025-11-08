package com.investments.tracker.service.transaction;

import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.enums.Status;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.Portfolio;
import com.investments.tracker.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellTransactionService {
    private final PortfolioService portfolioService;

    public boolean validateSellTransaction(String productName, BigDecimal transactionValue) {
        Optional<Portfolio> portfolioForProduct = portfolioService.findByProductName(productName);

        if (portfolioForProduct.isPresent() && portfolioForProduct.get().getStatus().equals(Status.ACTIVE)) {


            return true;
        } else {
            return false;
        }
        // check if product exists
        // check if we have enough of the product to sell

    }

    public BalanceResponse insertSellTransaction() {
        return null;
    }



    // TODO: Checking selling
//    private BalanceResponse sellTransaction(Balance currentBalance, BigDecimal transactionValue, TransactionRequest transactionRequestDTO) {
//        String productName = transactionRequestDTO.getProductName();
//        Optional<Portfolio> portfolioForProduct = this.portfolioRepository.findByProductName(productName);
//        if (!portfolioForProduct.isEmpty()) {
//            Portfolio portfolio = portfolioForProduct.get();
//            int currentQuantity = portfolio.getQuantity();
//            if (currentQuantity >= transactionRequestDTO.getQuantity()) {
//                Transaction transaction = createTransaction(transactionRequestDTO, transactionValue);
//                this.transactionRepository.save(transaction);
//
//                BigDecimal totalAmountOfInsertedFees = this.feeService.getTotalAmountOfInsertedFees(transactionRequestDTO, transaction.getId());
//
//                this.portfolioService.updatePortfolioWithSellTransaction(transactionRequestDTO, transactionValue);
//
//                Balance newBalance = this.balanceService.createNewBalanceFromTransaction(currentBalance, transaction, totalAmountOfInsertedFees);
//                this.balanceRepository.save(newBalance);
//                log.info("Successful [{}] transaction for date [{}] and product [{}]", transaction.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());
//
//                return createBalanceResponseDTO(newBalance);
//            } else {
//                log.info("Transaction cannot be created because there is not enough quantity of product [{}]", productName);
//                return createBalanceResponseDTO(null);
//            }
//        } else {
//            log.info("Transaction cannot be created because product [{}] does not exist in the portfolio", productName);
//            return createBalanceResponseDTO(null);
//        }
//    }
}
