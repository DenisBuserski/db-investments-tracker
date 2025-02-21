package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/withdrawal")
@Slf4j
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping("/out")
    public BalanceResponseDTO withdrawCash(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) throws Exception {
        log.info("Making withdrawal for [{} {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
        BalanceResponseDTO balance = this.withdrawalService.withdrawCash(withdrawalRequestDTO);
        return balance;
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<DepositResponseDTO> getDepositsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting withdrawals from [{}] to [{}]", from, to);
        List<DepositResponseDTO> deposits = this.withdrawalService.getAllWithdrawalsFromTo(from, to);
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return Collections.emptyList();
        } else {
            log.info("Deposits from [{}] to [{}] => [{}]", from, to, deposits);
            return deposits;
        }
    }

    @GetMapping("/get/all")
    public List<DepositResponseDTO> getAllDeposits() {
        log.info("Getting all withdrawals");
        List<DepositResponseDTO> deposits = this.withdrawalService.getAllWithdrawalsFromTo(
                LocalDate.of(2025, 1, 1),
                LocalDate.now());
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return Collections.emptyList();
        } else {
            log.info("Deposits [{}]", deposits);
            return deposits;
        }
    }

}
