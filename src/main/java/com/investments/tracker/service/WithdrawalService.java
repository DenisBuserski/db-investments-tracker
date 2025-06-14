package com.investments.tracker.service;

import com.investments.tracker.dto.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.dto.BalanceResponse.createBalanceResponseDTO;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;


@Service
@Slf4j
public class WithdrawalService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final BalanceService balanceService;

    @Autowired
    public WithdrawalService(
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
    
    public BalanceResponse withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO) {
        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();
            // FIX THIS IS VERY WRONG
            if (balance.getDate().isBefore(withdrawalRequestDTO.getDate())) {
                log.error("Withdrawal date cannot be before the latest balance date");
                return createBalanceResponseDTO(balance);
            } else {
                if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) >= 0) {
                    CashTransaction withdrawal = this.cashTransactionMapper.createCashtransaction(withdrawalRequestDTO, withdrawalMapper);
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
    public BigDecimal getTotalWithdrawalsAmount() {
        return null;
    }

}
