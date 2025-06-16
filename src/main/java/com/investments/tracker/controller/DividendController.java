package com.investments.tracker.controller;

import com.investments.tracker.dto.response.BalanceResponse;
import com.investments.tracker.dto.dividend.DividendRequestDTO;
import com.investments.tracker.service.DividendService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dividend")
@Slf4j
public class DividendController {
    private final DividendService dividendService;

    @Autowired
    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BalanceResponse> insertDividend(@RequestBody @Valid DividendRequestDTO dividendRequestDTO) {
        log.info("Inserting dividend for [{}]", dividendRequestDTO.getProductName());
        BalanceResponse balanceResponse = this.dividendService.insertDividend(dividendRequestDTO);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }

    // TODO:
    // getDividendsFromTo
    // getAllDividend
    // getTotalDividendsAmount
}
