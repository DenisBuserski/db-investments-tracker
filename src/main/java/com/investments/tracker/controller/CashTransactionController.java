package com.investments.tracker.controller;

import com.investments.tracker.dto.CashTransactionResponse;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.service.DepositService;
import com.investments.tracker.service.DividendService;
import com.investments.tracker.service.FeeService;
import com.investments.tracker.service.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.investments.tracker.utils.Constants.START_DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/cashtransaction")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET
)
@Slf4j
@Tag(name = "Cash transaction Controller", description = "REST methods for retrieving cash transaction data")
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
    // Used for deposits, withdrawals, dividends, fees

    // TODO: Add Pagination
    @GetMapping(value = "/get/from/{fromDate}/to/{toDate}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getCashTransactionsFromTo",
            summary = "Get cash transactions in range",
            description = "Get cash transactions in range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got cash transactions in range",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CashTransactionResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<CashTransactionResponse>> getCashTransactionsFromTo(
            @Parameter(description = "The type of cash transaction", required = true) @RequestParam("type") CashTransactionType type, // TODO: Fix only 1 type of fee
            @Parameter(description = "The start date of the range", required = true) @PathVariable(name = "fromDate") LocalDate from,
            @Parameter(description = "The end date of the range", required = true) @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting cashtransaction[{}] from [{}] to [{}]", type.name(), from, to);
        List<CashTransactionResponse> result = new ArrayList<>();
        switch (type) {
            case DEPOSIT:
                // result = this.depositService.getAllDepositsFromTo(from, to);
            case WITHDRAWAL:
                // result = this.withdrawalService.getAllWithdrawalsFromTo(from, to);
            case DIVIDEND:
                // result = this.dividendService.getAllDividendsFromTo(from, to);
            case FEE:
                // result = this.feeService.getAllFeesFromTo(from, to);
            default:
                return null;
        }

        return returnDepositList(result);
    }


    // TODO: Add Pagination
    @GetMapping(value = "/get/all", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "geAllDeposits",
            summary = "Get all deposits",
            description = "Get all deposits")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got all deposits",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DepositResponse.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<DepositResponse>> getAllDeposits() {
        log.info("Getting all deposits");
        List<DepositResponse> deposits = this.depositService.getAllDepositsFromTo(START_DATE, LocalDate.now());
        return returnDepositList(deposits);
    }

    @GetMapping(value = "/get/total/amount", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getTotalDepositsAmount",
            summary = "Get total deposits amount",
            description = "Get total deposits amount")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got total deposits amount",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BigDecimal.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BigDecimal> getTotalDepositsAmount() {
        log.info("Getting total amount of deposits");
        BigDecimal totalDepositsAmount = this.depositService.getTotalDepositsAmount();
        return new ResponseEntity<>(totalDepositsAmount, HttpStatus.OK);
    }

    private static ResponseEntity returnDepositList(List<DepositResponse> deposits) {
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found deposits - [{}]", deposits.size());
            return new ResponseEntity<>(deposits, HttpStatus.OK);
        }
    }
}
