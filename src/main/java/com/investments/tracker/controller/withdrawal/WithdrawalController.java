package com.investments.tracker.controller.withdrawal;

import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.service.withdrawal.WithdrawalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/withdrawals")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST }
)
@Slf4j
@Tag(name = "Withdrawal Controller", description = "Contains REST POST method for creating a withdrawal in the database")
@RequiredArgsConstructor
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    @PostMapping(value = "/out", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "insertWithdraw",
            summary = "Insert new withdrawal in the database",
            description = "Insert new withdrawal in the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Withdrawal created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BalanceResponse.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BalanceResponse> insertWithdraw(@RequestBody @Valid WithdrawalRequest withdrawalRequest) {
        log.info("Inserting withdrawal for [{} {}]", String.format("%.2f", withdrawalRequest.getAmount()), withdrawalRequest.getCurrency());
        BalanceResponse balanceResponse = withdrawalService.insertWithdraw(withdrawalRequest);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }

}
