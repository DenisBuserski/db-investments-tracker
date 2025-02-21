package com.investments.tracker.controller;

import com.investments.tracker.model.dto.TransactionRequestDTO;
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

    @PostMapping("/in")
    public void insertTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        log.info("Inserting transaction for date [{}] and product [{}]", transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());
        this.transactionService.insertTransaction(transactionRequestDTO);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
    public List<String> getTransactionsFromTo(
            @PathVariable(name = "fromDate") LocalDate from,
            @PathVariable(name = "toDate") LocalDate to) {
        return null;
    }


}
