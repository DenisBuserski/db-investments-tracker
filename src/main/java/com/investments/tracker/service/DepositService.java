package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.dto.BalanceResponse;
import com.investments.tracker.dto.deposit.DepositRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.dto.BalanceResponse.createBalanceResponseDTO;
import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;

@Service
@Slf4j
public class DepositService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final DepositMapper depositMapper;
    private final BalanceService balanceService;

    @Autowired
    public DepositService(
            CashTransactionRepository cashTransactionRepository,
            BalanceRepository balanceRepository,
            CashTransactionMapper cashTransactionMapper,
            DepositMapper depositMapper, BalanceService balanceService) {
        this.cashTransactionRepository = cashTransactionRepository;
        this.balanceRepository = balanceRepository;
        this.cashTransactionMapper = cashTransactionMapper;
        this.depositMapper = depositMapper;
        this.balanceService = balanceService;
    }

    // TODO: Check what is the currency of the Deposit, based on that decide how to save in the balance
    public BalanceResponse insertDeposit(DepositRequest depositRequest) {
        CashTransaction deposit = this.cashTransactionMapper.createCashtransaction(depositRequest, depositMapper);
        this.cashTransactionRepository.save(deposit);
        Balance newBalance;

        Optional<Balance> latestBalance = this.balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            newBalance = this.balanceService.createNewBalanceFromDeposit(latestBalance.get(), deposit);
        } else {
            newBalance = this.balanceService.createNewBalanceFromDeposit(null, deposit);
        }
        this.balanceRepository.save(newBalance);
        log.info("Deposit for [{} {}] successful", String.format("%.2f", deposit.getAmount()), deposit.getCurrency());
        return createBalanceResponseDTO(newBalance);
    }

    // TODO: What if we have deposits in 2 currencies
    public List<DepositResponse> getAllDepositsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> depositsResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(DEPOSIT, from, to);
        if (!depositsResult.isEmpty()) {
            return depositMapper.mapToResponseDTOList(depositsResult);
        }
        return Collections.emptyList();
    }

    // TODO: What if we have deposits in 2 currencies
    public BigDecimal getTotalDepositsAmount() {
        return this.cashTransactionRepository.getTotalAmountOf(DEPOSIT).orElse(BigDecimal.ZERO);
    }
}
