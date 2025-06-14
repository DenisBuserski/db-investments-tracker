package com.investments.tracker.controller;

import com.investments.tracker.dto.BalanceResponse;
import com.investments.tracker.dto.deposit.DepositRequest;
import com.investments.tracker.dto.deposit.DepositResponse;
import com.investments.tracker.service.DepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.investments.tracker.utils.Constants.START_DATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/deposits")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST, RequestMethod.GET }
)
@Slf4j
@Tag(name = "Deposit Controller", description = "REST methods for deposits")
public class DepositController {
    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping(value = "/in", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "insertDeposit",
            summary = "Insert new deposit in the database",
            description = "Insert new deposit in the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Deposit created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BalanceResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BalanceResponse> insertDeposit(@RequestBody @Valid DepositRequest depositRequest) {
        log.info("Inserting deposit for [{} {}]", String.format("%.2f", depositRequest.getAmount()), depositRequest.getCurrency());
        BalanceResponse balanceResponse = this.depositService.insertDeposit(depositRequest);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }

    // TODO: Add Pagination
    @GetMapping(value = "/get/from/{fromDate}/to/{toDate}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getDepositsFromTo",
            summary = "Get deposits in range",
            description = "Get deposits in range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got deposits in range",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepositResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<DepositResponse>> getDepositsFromTo(
            @Parameter(description = "The start date of the range", required = true) @PathVariable(name = "fromDate") LocalDate from,
            @Parameter(description = "The end date of the range", required = true) @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting deposits from [{}] to [{}]", from, to);
        List<DepositResponse> deposits = this.depositService.getAllDepositsFromTo(from, to);
        return returnDepositList(deposits);
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
