package com.investments.tracker.service;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.request.WithdrawalRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;


@Service
@Slf4j
public class WithdrawalService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final WithdrawalBalanceBuilderService withdrawalBalanceBuilderService;

    @Autowired
    public WithdrawalService(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            CashTransactionMapper cashTransactionMapper,
            WithdrawalMapper withdrawalMapper,
            WithdrawalBalanceBuilderService withdrawalBalanceBuilderService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.cashTransactionMapper = cashTransactionMapper;
        this.withdrawalMapper = withdrawalMapper;
        this.withdrawalBalanceBuilderService = withdrawalBalanceBuilderService;
    }

    // TODO: Check what is the currency of the Withdrawal, based on that decide how to save in the balance
    @Transactional
    public BalanceResponse insertWithdraw(WithdrawalRequest withdrawalRequest) {
        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();

            if (withdrawalRequest.getDate().isBefore(balance.getDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal date cannot be before the latest balance date");
            } else {
                if (balance.getBalance().compareTo(withdrawalRequest.getAmount()) >= 0) {
                    CashTransaction withdrawal = this.cashTransactionMapper.createCashtransaction(withdrawalRequest, withdrawalMapper);
                    this.cashTransactionRepository.save(withdrawal);

                    Balance newBalance = this.withdrawalBalanceBuilderService.createNewBalanceFromCashTransaction(balance, withdrawal);
                    this.balanceRepository.save(newBalance);
                    log.info("Withdrawal for [{} {}] successful", String.format("%.2f", withdrawal.getAmount()), withdrawal.getCurrency());
                    return createBalanceResponse(newBalance);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("You don't have enough money to withdraw. Current balance is [%s %s]", balance.getBalance(), "EUR"));
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal cannot be made, because no balance exists");
        }
    }

    // TODO: What if we have withdrawals in 2 currencies
    public List<CashTransactionResponse> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> withdrawalsResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, from, to);
        if (!withdrawalsResult.isEmpty()) {
            return withdrawalMapper.mapToResponseDTOList(withdrawalsResult);
        }
        return Collections.emptyList();
    }

    // TODO: What if we have withdrawals in 2 currencies
    public BigDecimal getTotalWithdrawalsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(WITHDRAWAL).orElse(BigDecimal.ZERO);
    }

}
