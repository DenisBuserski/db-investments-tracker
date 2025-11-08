package com.investments.tracker.service.withdrawal;

import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.withdrawal.WithdrawalRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.balance.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.validation.ValidationMessages.*;


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
        Optional<Balance> latestBalance = balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            Balance balance = latestBalance.get();

            if (withdrawalRequest.getDate().isBefore(balance.getDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE);
            } else {
                if (balance.getBalance().compareTo(withdrawalRequest.getAmount()) >= 0) {
                    CashTransaction withdrawal = cashTransactionMapper.createCashtransaction(withdrawalRequest, withdrawalMapper);
                    cashTransactionRepository.save(withdrawal);

                    Balance newBalance = withdrawalBalanceBuilderService.createBalanceFromCashTransaction(balance, withdrawal);
                    balanceRepository.save(newBalance);
                    log.info("Withdrawal for [{} {}] successful", String.format("%.2f", withdrawal.getAmount()), withdrawal.getCurrency());
                    return createBalanceResponse(newBalance);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NOT_ENOUGH_TO_WITHDRAWAL
                            .replace("%1", balance.getBalance().toString())
                            .replace("%2", "EUR")
                    );
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST);
        }
    }

    public List<CashTransactionResponse> getAllWithdrawalsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> withdrawalsResult = cashTransactionRepository.findByCashTransactionTypeAndDateBetween(WITHDRAWAL, from, to);
        if (!withdrawalsResult.isEmpty()) {
            return cashTransactionMapper.mapToResponseDTOList(withdrawalsResult, WITHDRAWAL);
        }
        return Collections.emptyList();
    }

    public BigDecimal getTotalWithdrawalsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(WITHDRAWAL).orElse(BigDecimal.ZERO);
    }

}
