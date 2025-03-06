package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.model.enums.CashTransactionType.*;

@Service
@Slf4j
public class DepositServiceImpl implements DepositService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public DepositServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
    }


    @Override
    public BalanceResponseDTO insertDeposit(DepositRequestDTO depositRequestDTO) {
        CashTransaction deposit = createCashtransaction(depositRequestDTO);
        this.cashTransactionRepository.save(deposit);
        Balance newBalance;

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            newBalance = createNewBalance(latestBalance.get(), deposit);
        } else {
            newBalance = createNewBalance(null, deposit);
        }
        this.balanceRepository.save(newBalance);
        log.info("Deposit for [{} {}] successful", deposit.getAmount(), deposit.getCurrency());
        return createBalanceResponseDTO(newBalance);
    }

    private static CashTransaction createCashtransaction(DepositRequestDTO depositRequestDTO) {
        return CashTransaction.builder()
                .date(depositRequestDTO.getDate())
                .cashTransactionType(DEPOSIT)
                .amount(depositRequestDTO.getAmount())
                .currency(depositRequestDTO.getCurrency())
                .description(depositRequestDTO.getDescription())
                .build();
    }

    private static Balance createNewBalance(Balance balance, CashTransaction deposit) {
        BigDecimal newBalanceAmount = balance == null ? deposit.getAmount() : balance.getBalance().add(deposit.getAmount());
        BigDecimal newTotalInvestments = balance == null ? BigDecimal.ZERO : balance.getTotalInvestments();
        BigDecimal newTotalDeposits = balance == null ? deposit.getAmount() : balance.getTotalDeposits().add(deposit.getAmount());
        BigDecimal newTotalWithdrawals = balance == null ? BigDecimal.ZERO : balance.getTotalWithdrawals();
        BigDecimal newTotalDividends = balance == null ? BigDecimal.ZERO : balance.getTotalDividends();
        BigDecimal newTotalFees = balance == null ? BigDecimal.ZERO : balance.getTotalFees();
        BigDecimal newLastPortfolioValue = balance == null ? BigDecimal.ZERO : balance.getLastPortfolioValue();

        return Balance.builder()
                .date(deposit.getDate())
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .build();
    }

    private static BalanceResponseDTO createBalanceResponseDTO(Balance newBalance) {
        return BalanceResponseDTO.builder()
                .date(newBalance.getDate())
                .balance(newBalance.getBalance())
                .totalInvestments(newBalance.getTotalInvestments())
                .totalDeposits(newBalance.getTotalDeposits())
                .totalWithdrawals(newBalance.getTotalWithdrawals())
                .totalDividends(newBalance.getTotalDividends())
                .totalFees(newBalance.getTotalFees())
                .lastPortfolioValue(newBalance.getLastPortfolioValue())
                .build();
    }

    @Override
    public List<DepositResponseDTO> getAllDepositsFromTo(LocalDate from, LocalDate to) {
        log.info("Getting all deposits from [{}] to [{}]", from, to);
        List<CashTransaction> depositsResult = this.cashTransactionRepository.getCashTransactionsFromTo(from, to, DEPOSIT);
        if (!depositsResult.isEmpty()) {
            List<DepositResponseDTO> deposits = new ArrayList<>();
            depositsResult.stream().forEach(deposit -> {
                DepositResponseDTO depositDTO = DepositResponseDTO.builder()
                        .date(deposit.getDate())
                        .amount(deposit.getAmount())
                        .currency(deposit.getCurrency())
                        .description(deposit.getDescription())
                        .build();
                deposits.add(depositDTO);
            });
            return deposits;
        }
        return Collections.emptyList();
    }

    @Override
    public BigDecimal getTotalDepositsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(DEPOSIT);
    }

}
