package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.service.DividendService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.info("Making deposit for [{} {}]");
        BalanceResponseDTO balanceResponseDTO = this.dividendService.insertDividend(dividendRequestDTO);
        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.CREATED);
    }
}
