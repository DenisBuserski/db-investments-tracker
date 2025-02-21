package com.investments.tracker.controller;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DividendRequestDTO;
import com.investments.tracker.service.DividendService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dividends")
@Slf4j
public class DividendController {
    private final DividendService dividendService;

    @Autowired
    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @PostMapping("/in")
    public BalanceResponseDTO insertDividend(@RequestBody @Valid DividendRequestDTO dividendRequestDTO) {
        log.info("Inserting dividend");
        return null;
    }
}
