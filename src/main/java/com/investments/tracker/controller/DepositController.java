package com.investments.tracker.controller;

import com.investments.tracker.model.dto.DepositRequestDTO;
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

    @PostMapping("/in")
    public void insertDeposit(@RequestBody @Valid DepositRequestDTO depositRequestDTO) {
        log.info("Making deposit for [{} - {}]", depositRequestDTO.getAmount(), depositRequestDTO.getCurrency());
        this.cashTransactionService.insertDeposit(depositRequestDTO);
        log.info("Deposit for [{} - {}] successful", depositRequestDTO.getAmount(), depositRequestDTO.getCurrency());
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

    // TODO: Get all deposits
}
