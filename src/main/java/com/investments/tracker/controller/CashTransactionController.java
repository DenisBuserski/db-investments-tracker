package com.investments.tracker.controller;

import com.investments.tracker.controller.response.CashTransactionResponse;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.service.*;
import com.investments.tracker.service.deposit.DepositService;
import com.investments.tracker.service.dividend.DividendService;
import com.investments.tracker.service.withdrawal.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.investments.tracker.common.util.Constants.START_DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/cashtransactions")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET
)
@Slf4j
@Tag(name = "Cash Transaction Controller", description = "REST methods for retrieving cash transaction data")
public class CashTransactionController {
    private final DepositService depositService;
    private final WithdrawalService withdrawalService;
    private final DividendService dividendService;
    private final FeeService feeService;

    public CashTransactionController(DepositService depositService,
                                     WithdrawalService withdrawalService,
                                     DividendService dividendService,
                                     FeeService feeService) {
        this.depositService = depositService;
        this.withdrawalService = withdrawalService;
        this.dividendService = dividendService;
        this.feeService = feeService;
    }

    // TODO: Add Pagination
    @GetMapping(value = "/get", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getCashTransactions",
            summary = "Get cash transactions in range",
            description = "Get cash transactions in range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got cash transactions in range",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CashTransactionResponse.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<CashTransactionResponse>> getCashTransactionsFromTo(
            @Parameter(description = "The type of cash transaction", required = true) @RequestParam("CashTransactionType") CashTransactionType type, // TODO: Fix only 1 type of fee,
            @Parameter(description = "The start date of the range. Format YYYY-MM-DD") @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "The end date of the range Format YYYY-MM-DD") @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        from = from == null ? START_DATE : from;
        to = to == null ? LocalDate.now() : to;
        log.info("Getting cash transactions type: {} from [{}] to [{}]", type.name().toUpperCase(), from, to);
        List<CashTransactionResponse> result = switch (type) {
            case DEPOSIT -> this.depositService.getAllDepositsFromTo(from, to);
            case WITHDRAWAL -> this.withdrawalService.getAllWithdrawalsFromTo(from, to);
            case DIVIDEND -> this.dividendService.getAllDividendsFromTo(from, to);
            case FEE -> this.feeService.getAllFeesFromTo(from, to);
        };

        return returnCashTransactionsResponseList(result);
    }


    @GetMapping(value = "/get/total/amount", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getTotalCashTransactionsAmount",
            summary = "Get total cash transactions amount",
            description = "Get total cash transactions amount")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got total cash transactions amount",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BigDecimal.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BigDecimal> getTotalCashTransactionsAmount(
            @Parameter(description = "The type of cash transaction", required = true) @RequestParam("CashTransactionType") CashTransactionType type) {
        log.info("Getting total amount of cash transactions with type: {}", type.name().toUpperCase());
        BigDecimal totalAmount = switch (type) {
            case DEPOSIT -> this.depositService.getTotalDepositsAmount();
            case WITHDRAWAL -> this.withdrawalService.getTotalWithdrawalsAmount();
            case DIVIDEND -> this.dividendService.getTotalDividendsAmount();
            case FEE -> this.feeService.getTotalFeesAmount();
        };
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }

    private static ResponseEntity<List<CashTransactionResponse>> returnCashTransactionsResponseList(List<CashTransactionResponse> cashTransactionResponses) {
        if (cashTransactionResponses.isEmpty()) {
            log.info("No cash transactions found");
            return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found [{}] cash transactions of type: {}", cashTransactionResponses.size(), cashTransactionResponses.get(0).type());
            return new ResponseEntity<>(cashTransactionResponses, HttpStatus.OK);
        }
    }
}
