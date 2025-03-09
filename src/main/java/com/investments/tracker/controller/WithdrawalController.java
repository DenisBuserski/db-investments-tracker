package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.service.WithdrawalService;
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
    private final WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping("/out")
    public BalanceResponseDTO withdrawCash(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} {}]", withdrawalRequestDTO.getAmount(), withdrawalRequestDTO.getCurrency());
        return this.withdrawalService.withdrawCash(withdrawalRequestDTO);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<WithdrawalResponseDTO> getWithdrawalsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting withdrawals from [{}] to [{}]", from, to);
        List<WithdrawalResponseDTO> withdrawals = this.withdrawalService.getAllWithdrawalsFromTo(from, to);
        if (withdrawals.isEmpty()) {
            return Collections.emptyList();
        } else {
            return withdrawals;
        }
    }

    @GetMapping("/get/all")
    public List<WithdrawalResponseDTO> getAllWithdrawals() {
        log.info("Getting all withdrawals");
        List<WithdrawalResponseDTO> withdrawals = this.withdrawalService.getAllWithdrawalsFromTo(
                LocalDate.of(2000, 1, 1),
                LocalDate.now());
        if (withdrawals.isEmpty()) {
            return Collections.emptyList();
        } else {
            return withdrawals;
        }
    }

    @GetMapping("/get/total/amount")
    public BigDecimal getTotalDepositAmount() {
        log.info("Getting total amount of withdrawals");
        return this.withdrawalService.getTotalWithdrawalsAmount();
    }

}
