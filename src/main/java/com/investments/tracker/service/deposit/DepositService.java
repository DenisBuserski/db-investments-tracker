package com.investments.tracker.service.deposit;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.DepositRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final DepositMapper depositMapper;
    private final DepositBalanceBuilderService depositBalanceBuilderService;

    @Transactional
    public BalanceResponse insertDeposit(DepositRequest depositRequest) {
        CashTransaction deposit = cashTransactionMapper.createCashtransaction(depositRequest, depositMapper);
        cashTransactionRepository.save(deposit);
        Balance newBalance;

        Optional<Balance> latestBalance = balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            newBalance = depositBalanceBuilderService.createBalanceFromCashTransaction(latestBalance.get(), deposit);
        } else {
            newBalance = depositBalanceBuilderService.createBalanceFromCashTransaction(null, deposit);
        }
        balanceRepository.save(newBalance);
        log.info("Deposit for [{} {}] successful", String.format("%.2f", deposit.getAmount()), deposit.getCurrency());
        return createBalanceResponse(newBalance);
    }

    public List<CashTransactionResponse> getAllDepositsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> depositsResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(DEPOSIT, from, to);
        if (!depositsResult.isEmpty()) {
            return depositMapper.mapToResponseDTOList(depositsResult);
        }
        return Collections.emptyList();
    }

    public BigDecimal getTotalDepositsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(DEPOSIT).orElse(BigDecimal.ZERO);
    }
}
