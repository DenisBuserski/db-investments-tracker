package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.model.enums.CashTransactionType;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.WithdrawalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) >= 0) {
                // -1 if balance.getBalance() is less than withdrawalRequestDTO.getAmount()
                // 0 if they are equal
                // 1 if balance.getBalance() is greater than withdrawalRequestDTO.getAmount()

                CashTransaction withdrawal = CashTransaction.builder()
                        .date(LocalDate.now())
                        .cashTransactionType(CashTransactionType.WITHDRAWAL)
                        .amount(withdrawalRequestDTO.getAmount())
                        .currency(withdrawalRequestDTO.getCurrency())
                        .build();
                this.cashTransactionRepository.save(withdrawal);
                log.info("Inserted withdrawal for [{} {}] in table [{}]", withdrawal.getAmount(), withdrawal.getCurrency(), "cash_transaction");

                Balance newBalance = Balance.builder()
                        .date(LocalDate.now())
                        .balance(balance.getBalance().subtract(withdrawal.getAmount()))
                        .totalDeposits(balance.getTotalDeposits())
                        .totalWithdrawals(balance.getTotalWithdrawals().add(withdrawal.getAmount()))
                        .totalDividends(balance.getTotalDividends())
                        .totalFees(balance.getTotalFees())
                        .build();
                this.balanceRepository.save(newBalance);
                log.info("Updating table [{}] for withdrawal [{} {}] ", "balance",  withdrawal.getAmount(), withdrawal.getCurrency());
                log.info("Withdrawal for [{} {}] successful", withdrawal.getAmount(), withdrawal.getCurrency());
                return createNewBalanceDTO(newBalance);
            } else {
                log.info("You don't have enough money to withdraw. Current balance is [{}]", balance.getBalance());
                return createNewBalanceDTO(balance);
            }
        } else {
            log.info("No balance found");
            return BalanceResponseDTO.builder()
                    .balance(BigDecimal.ZERO)
                    .totalDeposits(BigDecimal.ZERO)
                    .totalWithdrawals(BigDecimal.ZERO)
                    .totalDividends(BigDecimal.ZERO)
                    .totalFees(BigDecimal.ZERO)
                    .build();
        }

    }

    @Override
    public List<DepositResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        return List.of();
    }

    private static BalanceResponseDTO createNewBalanceDTO(Balance newBalance) {
        return BalanceResponseDTO.builder()
                .balance(newBalance.getBalance())
                .totalDeposits(newBalance.getTotalDeposits())
                .totalWithdrawals(newBalance.getTotalWithdrawals())
                .totalDividends(newBalance.getTotalDividends())
                .totalFees(newBalance.getTotalFees())
                .build();
    }

}
