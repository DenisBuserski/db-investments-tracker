package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.model.enums.Currency;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.DividendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.model.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.model.enums.Currency.EUR;
import static java.math.RoundingMode.CEILING;

@Service
@Slf4j
public class DividendServiceImpl implements DividendService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public DividendServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public BalanceResponseDTO insertDividend(DividendRequestDTO dividendRequestDTO) {
        LocalDate dividendDate = dividendRequestDTO.getDate();
        String productName = dividendRequestDTO.getProductName();
        int quantity = dividendRequestDTO.getQuantity();

        BigDecimal dividendAmountBeforeConversion = dividendRequestDTO.getDividendAmount();
        BigDecimal dividendTax = dividendRequestDTO.getDividendTax();
        BigDecimal exchangeRate = dividendRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : dividendRequestDTO.getExchangeRate();
        Currency dividendCurrency = dividendRequestDTO.getDividendCurrency();
        BigDecimal dividendAmount = dividendAmountBeforeConversion.subtract(dividendTax);

        if (!exchangeRate.equals(BigDecimal.ZERO)) {
            dividendAmount = dividendAmount.divide(exchangeRate, 10, CEILING).setScale(2, CEILING);
        }

        CashTransaction dividend = createCashtransaction(dividendDate, dividendAmount, productName, quantity, dividendTax, exchangeRate, dividendCurrency);
        this.cashTransactionRepository.save(dividend);
        Balance newBalance;

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            newBalance = createNewBalance(latestBalance.get(), dividend);
        } else {
            newBalance = createNewBalance(null, dividend);
        }

        return null;
    }



    private static CashTransaction createCashtransaction(
            LocalDate dividendDate,
            BigDecimal dividendAmount,
            String productName,
            int quantity,
            BigDecimal dividendTax,
            BigDecimal exchangeRate,
            Currency dividendCurrency) {
        String productDescription = String.format("Product:[%s]; Quantity:[%d]; DividendTax[%.2f]; ExchangeRate:[%.4f]; DividendCurrency[%s]",
                productName, quantity, dividendTax, exchangeRate, dividendCurrency.name());
        return CashTransaction.builder()
                .date(dividendDate)
                .cashTransactionType(DIVIDEND)
                .amount(dividendAmount)
                .currency(EUR)
                .description(productDescription)
                .build();
    }

    private static Balance createNewBalance(Balance balance, CashTransaction dividend) {
        BigDecimal newBalanceAmount = balance == null ? dividend.getAmount() : balance.getBalance().add(dividend.getAmount());
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? BigDecimal.ZERO : balance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? dividend.getAmount() : balance.getTotalDividends().add(dividend.getAmount());
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();

        return Balance.builder()
                .date(dividend.getDate())
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .build();
    }
}
