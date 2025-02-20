package com.investments.tracker.controller;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.dto.DepositDTO;
import com.investments.tracker.model.dto.DepositResultDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.service.CashTransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/withdrawal")
@Slf4j
public class WithdrawalController {
    private final CashTransactionService cashTransactionService;

    @Autowired
    public WithdrawalController(CashTransactionService cashTransactionService) {
        this.cashTransactionService = cashTransactionService;
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<DepositResultDTO> getDepositsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting deposits from [{}] to [{}]", from, to);
        List<DepositResultDTO> deposits = this.cashTransactionService.getAllDepositsFromTo(from, to);
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return Collections.emptyList();
        } else {
            log.info("Deposits from [{}] to [{}] => [{}]", from, to, deposits);
            return deposits;
        }
    }

    @GetMapping("/out")
    public void withdrawCash(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} - {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
        Balance balance = this.cashTransactionService.withdrawCash(withdrawalRequestDTO);
        if (balance == null) {
            log.info("You don't have any balance");
        } else if (balance.getBalance().compareTo(withdrawalRequestDTO.getAmount()) == 0) {

        } else {
            log.info("Successfully withdrawal [{} - {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
            log.info("New balance is [{}]", balance.getBalance());
        }
    }
}
