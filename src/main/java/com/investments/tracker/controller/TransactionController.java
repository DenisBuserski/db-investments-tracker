package com.investments.tracker.controller;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.TransactionRequest;
import com.investments.tracker.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST }
)
@Slf4j
@Tag(name = "Transaction Controller", description = "Contains REST methods for inserting and retrieving transactions from the database")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "insertTransaction",
            summary = "Insert new transaction in the database",
            description = "Insert new transaction in the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transaction created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BalanceResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BalanceResponse> insertTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        log.info("Creating [{}] transaction for product [{}] on [{}]", transactionRequest.getTransactionType(), transactionRequest.getProductName(), transactionRequest.getDate());
        BalanceResponse balanceResponse = this.transactionService.insertTransaction(transactionRequest);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }

    // TODO:
    // getTransactionsFromTo
}
