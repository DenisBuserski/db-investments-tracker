package com.investments.tracker.controller;

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
    public void withdrawCash(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} - {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
        BigDecimal balanceAmount = this.withdrawalService.withdrawCash(withdrawalRequestDTO);
        if (balanceAmount.compareTo(BigDecimal.ZERO) == 0) {
            log.info("You don't have any balance");
        } else if (balanceAmount.compareTo(BigDecimal.valueOf(-1)) == 0) {
            log.info("You don't have enough money to withdraw");
        } else {
            log.info("You have successfully withdrawn for [{}]", balanceAmount);
        }
    }
}
