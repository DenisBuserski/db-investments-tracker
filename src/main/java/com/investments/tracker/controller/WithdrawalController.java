package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;
import com.investments.tracker.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


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
    public BalanceResponseDTO withdrawCash(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
        BalanceResponseDTO balance = this.withdrawalService.withdrawCash(withdrawalRequestDTO);
        if (balance.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            return balance;
        } else {
            log.info("Withdrawal for [{} {}] successful", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
            return balance;
        }
    }
}
