package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Dividend;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.model.dto.dividend.DividendResponseDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.repository.DividendRepository;
import com.investments.tracker.service.DividendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static com.investments.tracker.model.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.model.enums.Currency.EUR;
import static java.math.RoundingMode.CEILING;

@Service
@Slf4j
public class DividendServiceImpl implements DividendService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final DividendRepository dividendRepository;

    @Autowired
    public DividendServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            DividendRepository dividendRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.dividendRepository = dividendRepository;
    }

    @Override
    public BalanceResponseDTO insertDividend(DividendRequestDTO dividendRequestDTO) {
        BigDecimal exchangeRate = dividendRequestDTO.getExchangeRate() == null ? BigDecimal.ZERO : dividendRequestDTO.getExchangeRate();
        BigDecimal dividendAmountBeforeConversion = calculateDividendAmount(dividendRequestDTO);
        BigDecimal dividendAmountAfterConversion = dividendConversion(exchangeRate, dividendAmountBeforeConversion);
        Balance newBalance;

        CashTransaction dividend = createCashtransaction(dividendRequestDTO, dividendAmountAfterConversion);
        this.cashTransactionRepository.save(dividend);

        Dividend dividendEntity = creteDividend(dividendRequestDTO);
        this.dividendRepository.save(dividendEntity);

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            newBalance = createNewBalance(latestBalance.get(), dividend);
        } else {
            newBalance = createNewBalance(null, dividend);
        }

        this.balanceRepository.save(newBalance);
        log.info("Dividend for product [{}] created successfully", dividendRequestDTO.getProductName());
        return createBalanceResponseDTO(newBalance);
    }

    private static BigDecimal calculateDividendAmount(DividendRequestDTO dividendRequestDTO) {
        BigDecimal dividendAmountBeforeConversion = dividendRequestDTO.getDividendAmount();
        BigDecimal dividendTaxBeforeConversion = dividendRequestDTO.getDividendTax();
        return dividendAmountBeforeConversion.subtract(dividendTaxBeforeConversion);
    }

    private static BigDecimal dividendConversion(BigDecimal exchangeRate, BigDecimal dividendAmountBeforeConversion) {
        if (!exchangeRate.equals(BigDecimal.ZERO)) {
            return dividendAmountBeforeConversion.divide(exchangeRate, 10, CEILING).setScale(2, CEILING);
        } else {
            return dividendAmountBeforeConversion;
        }
    }

    private static CashTransaction createCashtransaction(DividendRequestDTO dividendRequestDTO, BigDecimal dividendAmount) {
        String cashtransactionDescription = String.format("Dividend for product [%s]", dividendRequestDTO.getProductName());
        return CashTransaction.builder()
                .date(dividendRequestDTO.getDate())
                .cashTransactionType(DIVIDEND)
                .amount(dividendAmount)
                .currency(EUR)
                .description(cashtransactionDescription)
                .build();
    }

    private static Dividend creteDividend(DividendRequestDTO dividendRequestDTO) {
        int quantity = dividendRequestDTO.getQuantity();
        BigDecimal dividendAmount = dividendRequestDTO.getDividendAmount();
        BigDecimal dividendTaxAmount = dividendRequestDTO.getDividendTax();
        BigDecimal dividendAmountPerShare = dividendAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);
        BigDecimal dividendTaxPerShare = dividendTaxAmount.divide(BigDecimal.valueOf(quantity), 10, CEILING);

        return Dividend.builder()
                .date(dividendRequestDTO.getDate())
                .productName(dividendRequestDTO.getProductName())
                .quantity(quantity)
                .dividendAmount(dividendAmount)
                .dividendTaxAmount(dividendTaxAmount)
                .dividendAmountPerShare(dividendAmountPerShare)
                .dividendTaxAmountPerShare(dividendTaxPerShare)
                .exchangeRate(dividendRequestDTO.getExchangeRate())
                .currency(dividendRequestDTO.getDividendCurrency())
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

    // TODO: Implement methods
    @Override
    public List<DividendResponseDTO> getAllDividendsFromTo(LocalDate from, LocalDate to) {
        return List.of();
    }

    @Override
    public BigDecimal getTotalDividendsAmount() {
        return null;
    }
}
