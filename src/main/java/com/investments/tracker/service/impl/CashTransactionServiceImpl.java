package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.DepositDTO;
import com.investments.tracker.model.dto.DepositResultDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.CashTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CashTransactionServiceImpl implements CashTransactionService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public CashTransactionServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public List<DepositResultDTO> getAllDepositsFromTo(LocalDate from, LocalDate to) {
        log.info("Getting all deposits from [{}] to [{}]", from, to);
        List<CashTransaction> depositsResult = this.cashTransactionRepository.getDepositsFromTo(from, to);
        if (!depositsResult.isEmpty()) {
            List<DepositResultDTO> deposits = new ArrayList<>();
            depositsResult.stream().forEach(deposit -> {
                DepositResultDTO depositDTO = DepositResultDTO.builder()
                        .date(deposit.getDate())
                        .amount(deposit.getAmount())
                        .currency(deposit.getCurrency())
                        .build();
                deposits.add(depositDTO);
            });
            return deposits;
        }
        return Collections.emptyList();
    }

    @Override
    public void insertDeposit(DepositDTO depositDTO) {
        CashTransaction deposit = CashTransaction.builder()
                .date(LocalDate.now())
                .cashTransactionType(CashTransactionType.DEPOSIT)
                .amount(depositDTO.getAmount())
                .currency(depositDTO.getCurrency())
                .build();
        this.cashTransactionRepository.save(deposit);
        log.info("Inserted deposit for [{} - {}] ", deposit.getAmount(), deposit.getCurrency());

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();
            Balance newBalance = Balance.builder()
                    .date(LocalDate.now())
                    .balance(balance.getBalance().add(deposit.getAmount()))
                    .totalDeposits(balance.getTotalDeposits().add(deposit.getAmount()))
                    .totalWithdrawals(balance.getTotalWithdrawals())
                    .totalDividends(balance.getTotalDividends())
                    .totalFees(balance.getTotalFees())
                    .build();
            this.balanceRepository.save(newBalance);
            log.info("Updating balance table for deposit [{} - {}] ", deposit.getAmount(), deposit.getCurrency());
        } else {
            Balance newBalance = Balance.builder()
                    .date(LocalDate.now())
                    .balance(deposit.getAmount())
                    .totalDeposits(deposit.getAmount())
                    .totalWithdrawals(BigDecimal.ZERO)
                    .totalDividends(BigDecimal.ZERO)
                    .totalFees(BigDecimal.ZERO)
                    .build();
            this.balanceRepository.save(newBalance);
            log.info("Inserting in balance table for the first time for deposit [{} - {}]", deposit.getAmount(), deposit.getCurrency());
        }
    }

    @Override
    public Balance withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO) {
        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();
            // Check if we have enough money
            if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) > 0) {
                // -1 if balance.getBalance() is less than withdrawalRequestDTO.getAmount()
                // 0 if they are equal
                // 1 if balance.getBalance() is greater than withdrawalRequestDTO.getAmount()

                // Make withdraw
                CashTransaction withdrawal = CashTransaction.builder()
                        .date(LocalDate.now())
                        .cashTransactionType(CashTransactionType.WITHDRAWAL)
                        .amount(withdrawalRequestDTO.getAmount())
                        .currency(withdrawalRequestDTO.getCurrency())
                        .build();
                this.cashTransactionRepository.save(withdrawal);
                Balance newBalance = Balance.builder()
                        .date(LocalDate.now())
                        .balance(balance.getBalance().subtract(withdrawalRequestDTO.getAmount()))
                        .totalDeposits(balance.getTotalDeposits())
                        .totalWithdrawals(balance.getTotalWithdrawals().add(withdrawalRequestDTO.getAmount()))
                        .totalDividends(balance.getTotalDividends())
                        .totalFees(balance.getTotalFees())
                        .build();
                this.balanceRepository.save(newBalance);
                return newBalance;
            } else {
                return null;
            }
        } else {
            return null;
        }



    }
}
