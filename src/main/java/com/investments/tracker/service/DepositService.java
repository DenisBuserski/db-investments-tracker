package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;
import com.investments.tracker.model.mapper.CashTransactionMapper;
import com.investments.tracker.model.mapper.DepositMapper;
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

import static com.investments.tracker.model.dto.BalanceResponseDTO.createBalanceResponseDTO;
import static com.investments.tracker.model.enums.CashTransactionType.*;

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

    public BalanceResponseDTO insertDeposit(DepositRequestDTO depositRequestDTO) {
        CashTransaction deposit = this.cashTransactionMapper.createCashtransaction(depositRequestDTO, depositMapper);
        this.cashTransactionRepository.save(deposit);
        Balance newBalance;

        Optional<Balance> latestBalance = this.balanceRepository.getLatestBalance();
        if (latestBalance.isPresent()) {
            newBalance = this.balanceService.createNewBalanceFromDeposit(latestBalance.get(), deposit);
        } else {
            newBalance = this.balanceService.createNewBalanceFromDeposit(null, deposit);
        }
        this.balanceRepository.save(newBalance);
        log.info("Deposit for [{} {}] successful", String.format("%.2f", deposit.getAmount()), deposit.getCurrency());
        return createBalanceResponseDTO(newBalance);
    }

    public List<DepositResponseDTO> getAllDepositsFromTo(LocalDate from, LocalDate to) {
        List<CashTransaction> depositsResult = this.cashTransactionRepository.findByCashTransactionTypeAndDateBetween(DEPOSIT, from, to);
        if (!depositsResult.isEmpty()) {
            return depositMapper.mapToResponseDTOList(depositsResult);
        }
        return Collections.emptyList();
    }

    public BigDecimal getTotalDepositsAmount() {
        return this.cashTransactionRepository.getTotalDepositsAmount().orElse(BigDecimal.ZERO);
    }

}
