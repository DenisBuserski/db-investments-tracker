package com.investments.tracker.controller;

import com.investments.tracker.dto.BalanceResponseDTO;
import com.investments.tracker.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.dto.withdraw.WithdrawalResponseDTO;
import com.investments.tracker.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/withdrawals")
@Slf4j
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping("/out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BalanceResponseDTO> makeWithdraw(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} {}]", String.format("%.2f", withdrawalRequestDTO.getAmount()), withdrawalRequestDTO.getCurrency());
        BalanceResponseDTO balanceResponseDTO = this.withdrawalService.withdrawCash(withdrawalRequestDTO);
        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.OK);
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
    public BigDecimal getTotalWithdrawalsAmount() {
        log.info("Getting total amount of withdrawals");
        return this.withdrawalService.getTotalWithdrawalsAmount();
    }

}
