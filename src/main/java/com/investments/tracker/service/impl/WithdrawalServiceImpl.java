package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.*;
import com.investments.tracker.model.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.WithdrawalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.model.enums.CashTransactionType.WITHDRAWAL;


@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public WithdrawalServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public BalanceResponseDTO withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO) {
        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();
            // Throw exception if try to insert withdrawal with date before the balance
            if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) >= 0) {
                CashTransaction withdrawal = createCashtransaction(withdrawalRequestDTO);
                this.cashTransactionRepository.save(withdrawal);

                Balance newBalance = createNewBalance(balance, withdrawal);
                this.balanceRepository.save(newBalance);
                log.info("Withdrawal for [{} {}] successful", withdrawal.getAmount(), withdrawal.getCurrency());
                return createNewBalanceDTO(newBalance);
            } else {
                log.info("You don't have enough money to withdraw. Current balance is [{}]", balance.getBalance());
                return createNewBalanceDTO(balance);
            }
        } else {
            log.info("No balance found");
            return createNewBalanceDTO(null);
        }
    }

    private static CashTransaction createCashtransaction(WithdrawalRequestDTO withdrawalRequestDTO) {
        return CashTransaction.builder()
                .date(withdrawalRequestDTO.getDate())
                .cashTransactionType(CashTransactionType.WITHDRAWAL)
                .amount(withdrawalRequestDTO.getAmount())
                .currency(withdrawalRequestDTO.getCurrency())
                .description(withdrawalRequestDTO.getDescription())
                .build();
    }

    private static Balance createNewBalance(Balance balance, CashTransaction withdrawal) {
        return Balance.builder()
                .date(withdrawal.getDate())
                .balance(balance.getBalance().subtract(withdrawal.getAmount()))
                .totalInvestments(balance.getTotalInvestments())
                .totalDeposits(balance.getTotalDeposits())
                .totalWithdrawals(balance.getTotalWithdrawals().add(withdrawal.getAmount()))
                .totalDividends(balance.getTotalDividends())
                .totalFees(balance.getTotalFees())
                .lastPortfolioValue(balance.getLastPortfolioValue())
                .build();
    }

    private static BalanceResponseDTO createNewBalanceDTO(Balance newBalance) {
        LocalDate newDate = newBalance == null ? LocalDate.now(): newBalance.getDate();
        BigDecimal newBalanceAmount = newBalance == null ? BigDecimal.ZERO : newBalance.getBalance();
        BigDecimal newTotalInvestments = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalInvestments();
        BigDecimal newTotalDeposits = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalWithdrawals();
        BigDecimal newTotalDividends = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDividends();
        BigDecimal newTotalFees = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalFees();
        BigDecimal newLastPortfolioValue = newBalance == null ? BigDecimal.ZERO : newBalance.getLastPortfolioValue();

        return BalanceResponseDTO.builder()
                .date(newDate)
                .balance(newBalanceAmount)
                .totalInvestments(newTotalInvestments)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .lastPortfolioValue(newLastPortfolioValue)
                .build();
    }

    @Override
    public List<WithdrawalResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        log.info("Getting all withdrawals from [{}] to [{}]", from, to);
        List<CashTransaction> withdrawalResult = this.cashTransactionRepository.getCashTransactionsFromTo(from, to, WITHDRAWAL);
        if (!withdrawalResult.isEmpty()) {
            List<WithdrawalResponseDTO> withdrawals = new ArrayList<>();
            withdrawalResult.stream().forEach(withdrawal -> {
                WithdrawalResponseDTO withdrawalDTO = WithdrawalResponseDTO.builder()
                        .date(withdrawal.getDate())
                        .amount(withdrawal.getAmount())
                        .currency(withdrawal.getCurrency())
                        .description(withdrawal.getDescription())
                        .build();
                withdrawals.add(withdrawalDTO);
            });
            return withdrawals;
        }
        return Collections.emptyList();
    }



    @Override
    public BigDecimal getTotalWithdrawalsAmount() {
        //return this.cashTransactionRepository.getTotalAmountOf(WITHDRAWAL);
        return null;
    }

}
