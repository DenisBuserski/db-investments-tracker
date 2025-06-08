package com.investments.tracker.controller;

import com.investments.tracker.dto.BalanceResponseDTO;
import com.investments.tracker.dto.deposit.DepositRequestDTO;
import com.investments.tracker.dto.deposit.DepositResponseDTO;
import com.investments.tracker.service.DepositService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/v1/deposits")
@Slf4j
@Tag(name = "Deposit Controller", description = "REST methods for deposits")
public class DepositController {
    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/in")
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
                                    schema = @Schema(implementation = BalanceResponseDTO.class))
                    })
    })
    public ResponseEntity<BalanceResponseDTO> insertDeposit(@RequestBody @Valid DepositRequestDTO depositRequestDTO) {
        log.info("Inserting deposit for [{} {}]", String.format("%.2f", depositRequestDTO.getAmount()), depositRequestDTO.getCurrency());
        BalanceResponseDTO balanceResponseDTO = this.depositService.insertDeposit(depositRequestDTO);
        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get/from/{fromDate}/to/{toDate}")
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
                                    schema = @Schema(implementation = DepositResponseDTO.class))
                    })
    })
    public ResponseEntity<List<DepositResponseDTO>> getDepositsFromTo(
            @Parameter(description = "The start date of the range", required = true) @PathVariable(name = "fromDate") LocalDate from,
            @Parameter(description = "The end date of the range", required = true) @PathVariable(name = "toDate") LocalDate to) {
        log.info("Getting deposits from [{}] to [{}]", from, to);
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(from, to);
        return returnDepositList(deposits);
    }


    // TODO: Add Pagination
    @GetMapping("/get/all")
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
                                    schema = @Schema(implementation = DepositResponseDTO.class))
                    })
    })
    public ResponseEntity<List<DepositResponseDTO>> getAllDeposits() {
        log.info("Getting all deposits");
        List<DepositResponseDTO> deposits = this.depositService.getAllDepositsFromTo(START_DATE, LocalDate.now());
        return returnDepositList(deposits);
    }

    @GetMapping("/get/total/amount")
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
                    })
    })
    public ResponseEntity<BigDecimal> getTotalDepositsAmount() {
        log.info("Getting total amount of deposits");
        BigDecimal totalDepositsAmount = this.depositService.getTotalDepositsAmount();
        return new ResponseEntity<>(totalDepositsAmount, HttpStatus.OK);
    }

    private static ResponseEntity returnDepositList(List<DepositResponseDTO> deposits) {
        if (deposits.isEmpty()) {
            log.info("No deposits found");
            return new ResponseEntity(Collections.EMPTY_LIST, HttpStatus.OK);
        } else {
            log.info("Found deposits - [{}]", deposits.size());
            return new ResponseEntity<>(deposits, HttpStatus.OK);
        }
    }

}
