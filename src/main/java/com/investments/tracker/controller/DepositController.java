package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;
import com.investments.tracker.service.DepositService;
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

import static com.investments.tracker.utils.Constants.START_DATE;

@RestController
@RequestMapping("/api/deposit")
@Slf4j
public class DepositController {
    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BalanceResponseDTO> insertDeposit(@RequestBody @Valid DepositRequestDTO depositRequestDTO) {
        log.info("Making deposit for [{} {}]", String.format("%.2f", depositRequestDTO.getAmount()), depositRequestDTO.getCurrency());
        BalanceResponseDTO balanceResponseDTO = this.depositService.insertDeposit(depositRequestDTO);
        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DepositResponseDTO>> getDepositsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting deposits from [{}] to [{}]", from, to);
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(from, to);
        return returnDepositList(deposits);
    }



    @GetMapping("/get/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DepositResponseDTO>>  getAllDeposits() {
        log.info("Getting all deposits");
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(START_DATE, LocalDate.now());
        return returnDepositList(deposits);
    }

    @GetMapping("/get/total/amount")
    public BigDecimal getTotalDepositAmount() {
        log.info("Getting total amount of deposits");
        return this.depositService.getTotalDepositsAmount();
    }

    private static ResponseEntity returnDepositList(List<DepositResponseDTO> deposits) {
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found deposits - [{}]", deposits.size());
            return new ResponseEntity<>(deposits, HttpStatus.OK);
        }
    }

}
