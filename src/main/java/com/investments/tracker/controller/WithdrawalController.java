package com.investments.tracker.controller;

import com.investments.tracker.dto.response.BalanceResponse;
import com.investments.tracker.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/withdrawals")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST, RequestMethod.GET }
)
@Slf4j
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    @Autowired
    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @PostMapping("/out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BalanceResponse> makeWithdraw(@RequestBody @Valid WithdrawalRequestDTO withdrawalRequestDTO) {
        log.info("Making withdrawal for [{} {}]", String.format("%.2f", withdrawalRequestDTO.getAmount()), withdrawalRequestDTO.getCurrency());
        BalanceResponse balanceResponse = this.withdrawalService.withdrawCash(withdrawalRequestDTO);
        return new ResponseEntity<>(balanceResponse, HttpStatus.OK);
    }

}
