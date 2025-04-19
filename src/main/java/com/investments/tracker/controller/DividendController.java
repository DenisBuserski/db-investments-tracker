package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.model.dto.dividend.DividendResponseDTO;
import com.investments.tracker.service.DividendService;
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
@RequestMapping("/dividend")
@Slf4j
public class DividendController {
    private final DividendService dividendService;

    @Autowired
    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BalanceResponseDTO> insertDividend(@RequestBody @Valid DividendRequestDTO dividendRequestDTO) {
        log.info("Inserting dividend for [{}]", dividendRequestDTO.getProductName());
        BalanceResponseDTO balanceResponseDTO = this.dividendService.insertDividend(dividendRequestDTO);
        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DividendResponseDTO>> getDividendsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting dividends from [{}] to [{}]", from, to);
        List<DividendResponseDTO> dividends = this.dividendService.getAllDividendsFromTo(from, to);
        if (dividends.isEmpty()) {
            log.info("No dividends found");
            return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found dividends - [{}]", dividends.size());
            return new ResponseEntity<>(dividends, HttpStatus.OK);
        }
    }

    @GetMapping("/get/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DividendResponseDTO>>  getAllDeposits() {
        log.info("Getting all dividends");
        List<DividendResponseDTO> dividends = this.dividendService.getAllDividendsFromTo(START_DATE, LocalDate.now());
        if (dividends.isEmpty()) {
            log.info("No dividends found");
            return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found dividends - [{}]", dividends.size());
            return new ResponseEntity<>(dividends, HttpStatus.OK);
        }
    }

    @GetMapping("/get/total/amount")
    public BigDecimal getTotalDividendsAmount() {
        log.info("Getting total amount of dividends");
        return this.dividendService.getTotalDividendsAmount();
    }
}
