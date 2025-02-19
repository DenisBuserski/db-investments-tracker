package com.investments.tracker.controller;

import com.investments.tracker.model.dto.DepositDTO;
import com.investments.tracker.model.dto.DepositResultDTO;
import com.investments.tracker.service.CashTransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/deposit")
@Slf4j
public class DepositController {
    private final CashTransactionService cashTransactionService;

    @Autowired
    public DepositController(CashTransactionService cashTransactionService) {
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

    @PostMapping("/in")
    public void deposit(@RequestBody @Valid DepositDTO depositDTO) {
        log.info("Making deposit for [{} - {}]", depositDTO.getAmount(),depositDTO.getCurrency());
        this.cashTransactionService.insertDeposit(depositDTO);
        log.info("Deposit for [{} - {}] successful", depositDTO.getAmount(),depositDTO.getCurrency());
    }
}
