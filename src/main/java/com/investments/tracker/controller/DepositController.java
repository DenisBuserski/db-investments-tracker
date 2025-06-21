package com.investments.tracker.controller;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.DepositRequest;
import com.investments.tracker.service.DepositService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/deposits")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST }
)
@Slf4j
@Tag(name = "Deposit Controller", description = "Contains REST POST method for inserting a deposit in database")
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

}
