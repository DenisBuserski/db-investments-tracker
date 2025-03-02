package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;
import com.investments.tracker.service.DepositService;
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
    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/in")
    public BalanceResponseDTO insertDeposit(@RequestBody @Valid DepositRequestDTO depositRequestDTO) {
        log.info("Making deposit for [{} {}]", depositRequestDTO.getAmount(), depositRequestDTO.getCurrency());
        return this.depositService.insertDeposit(depositRequestDTO);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<DepositResponseDTO> getDepositsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting deposits from [{}] to [{}]", from, to);
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(from, to);
        if (deposits.isEmpty()) {
            return Collections.emptyList();
        } else {
            return deposits;
        }
    }

    @GetMapping("/get/all")
    public List<DepositResponseDTO> getAllDeposits() {
        log.info("Getting all deposits");
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(
                LocalDate.of(2000, 1, 1),
                LocalDate.now());
        if (deposits.isEmpty()) {
            return Collections.emptyList();
        } else {
            return deposits;
        }
    }

}
