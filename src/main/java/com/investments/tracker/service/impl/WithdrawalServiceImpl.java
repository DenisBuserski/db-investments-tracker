package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.*;
import com.investments.tracker.model.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.model.mapper.CashTransactionMapper;
import com.investments.tracker.model.mapper.WithdrawalMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.BalanceService;
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

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static com.investments.tracker.model.enums.CashTransactionType.WITHDRAWAL;


@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final BalanceService balanceService;

    @Autowired
    public WithdrawalServiceImpl(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            CashTransactionMapper cashTransactionMapper,
            WithdrawalMapper withdrawalMapper,
            BalanceService balanceService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.cashTransactionMapper = cashTransactionMapper;
        this.withdrawalMapper = withdrawalMapper;
        this.balanceService = balanceService;
    }

    @Override
    public BalanceResponseDTO withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO) {
        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();
            if (balance.getDate().isBefore(withdrawalRequestDTO.getDate())) {
                log.error("Withdrawal date cannot be before the latest balance date");
                return createBalanceResponseDTO(balance);
            } else {
                if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) >= 0) {
                    CashTransaction withdrawal = this.cashTransactionMapper.createCashTransaction(withdrawalRequestDTO, withdrawalMapper);
                    this.cashTransactionRepository.save(withdrawal);

                    Balance newBalance = this.balanceService.createNewBalanceFromWithdrawal(balance, withdrawal);
                    this.balanceRepository.save(newBalance);
                    log.info("Withdrawal for [{} {}] successful", String.format("%.2f", withdrawal.getAmount()), withdrawal.getCurrency());
                    return createBalanceResponseDTO(newBalance);
                } else {
                    log.info("You don't have enough money to withdraw. Current balance is [{}]", balance.getBalance());
                    return createBalanceResponseDTO(balance);
                }
            }
        } else {
            log.info("Withdrawal cannot be made, because no balance exists");
            return createBalanceResponseDTO(null);
        }
    }

    // TODO: Refactor
    @Override
    public List<WithdrawalResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        log.info("Getting all withdrawals from [{}] to [{}]", from, to);
        List<CashTransaction> withdrawalResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, from, to);
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

    // TODO: Refactor
    @Override
    public BigDecimal getTotalWithdrawalsAmount() {
        return null;
    }

}
