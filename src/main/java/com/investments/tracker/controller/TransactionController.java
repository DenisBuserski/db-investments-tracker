package com.investments.tracker.controller;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.dto.transaction.TransactionRequestDTO;
import com.investments.tracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BalanceResponse> insertTransaction(@RequestBody @Valid TransactionRequestDTO transactionRequestDTO) {
        log.info("Creating [{}] transaction for date [{}] and product [{}]", transactionRequestDTO.getTransactionType(), transactionRequestDTO.getDate(), transactionRequestDTO.getProductName());
        BalanceResponse balanceResponse = this.transactionService.insertTransaction(transactionRequestDTO);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }

    // TODO:
    // getTransactionsFromTo
    // getAllTransactions




}
