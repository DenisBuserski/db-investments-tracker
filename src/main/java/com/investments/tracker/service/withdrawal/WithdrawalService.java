package com.investments.tracker.service.withdrawal;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;


@Service
@Slf4j
@RequiredArgsConstructor
public class WithdrawalService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final WithdrawalMapper withdrawalMapper;
    private final WithdrawalBalanceBuilderService withdrawalBalanceBuilderService;

    @Transactional
    public BalanceResponse insertWithdraw(WithdrawalRequest withdrawalRequest) {
        Optional<Balance> latestBalance = balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();

            if (withdrawalRequest.getDate().isBefore(balance.getDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Withdrawal date cannot be before the latest balance date");
            } else {
                if (balance.getBalance().compareTo(withdrawalRequest.getAmount()) >= 0) {
                    CashTransaction withdrawal = this.cashTransactionMapper.createCashtransaction(withdrawalRequest, withdrawalMapper);
                    this.cashTransactionRepository.save(withdrawal);

                    Balance newBalance = this.withdrawalBalanceBuilderService.createBalanceFromCashTransaction(balance, withdrawal);
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

    public List<CashTransactionResponse> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> withdrawalsResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, from, to);
        if (!withdrawalsResult.isEmpty()) {
            return withdrawalMapper.mapToResponseDTOList(withdrawalsResult);
        }
        return Collections.emptyList();
    }

    public BigDecimal getTotalWithdrawalsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(WITHDRAWAL).orElse(BigDecimal.ZERO);
    }

}
