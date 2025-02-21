package com.investments.tracker.service.impl;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositRequestDTO;
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
    public List<DepositResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        return List.of();
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

                CashTransaction withdrawal = createCashtransaction(withdrawalRequestDTO);
                this.cashTransactionRepository.save(withdrawal);
                log.info("Inserted withdrawal for [{} {}] in table [{}]", withdrawal.getAmount(), withdrawal.getCurrency(), "cash_transaction");

                Balance newBalance = createNewBalance(balance, withdrawal);
                this.balanceRepository.save(newBalance);
                log.info("Inserted withdrawal for [{} {}] in table [{}]", withdrawal.getAmount(), withdrawal.getCurrency(), "balance");
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
                .date(LocalDate.now())
                .cashTransactionType(CashTransactionType.WITHDRAWAL)
                .amount(withdrawalRequestDTO.getAmount())
                .currency(withdrawalRequestDTO.getCurrency())
                .build();
    }

    private static Balance createNewBalance(Balance balance, CashTransaction withdrawal) {
        return Balance.builder()
                .date(LocalDate.now())
                .balance(balance.getBalance().subtract(withdrawal.getAmount()))
                .totalDeposits(balance.getTotalDeposits())
                .totalWithdrawals(balance.getTotalWithdrawals().add(withdrawal.getAmount()))
                .totalDividends(balance.getTotalDividends())
                .totalFees(balance.getTotalFees())
                .build();
    }

    private static BalanceResponseDTO createNewBalanceDTO(Balance newBalance) {
        BigDecimal newBalanceAmount = newBalance == null ? BigDecimal.ZERO : newBalance.getBalance();
        BigDecimal newTotalDeposits = newBalance == null ? BigDecimal.ZERO: newBalance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalWithdrawals();
        BigDecimal newTotalDividends = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDividends();
        BigDecimal newTotalFees = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalFees();

        return BalanceResponseDTO.builder()
                .balance(newBalanceAmount)
                .totalDeposits(newTotalDeposits)
                .totalWithdrawals(newTotalWithdrawals)
                .totalDividends(newTotalDividends)
                .totalFees(newTotalFees)
                .build();
    }

}
