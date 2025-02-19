package com.investments.tracker.controller;

import com.investments.tracker.model.dto.TransactionDTO;
import com.investments.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<String> getTransactionsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        return null;
    }

    @PostMapping("/in")
    public void insertTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        this.transactionService.insertTransaction(transactionDTO);
    }
}
