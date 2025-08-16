package com.investments.tracker.controller.balance;

import com.investments.tracker.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/balance")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET
)
@Slf4j
@Tag(name = "Balance Controller", description = "REST endpoint for retrieving the latest balance data")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    // TODO: Redis might be needed here
    @GetMapping(value = "/get", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            operationId = "getLatestBalanceData",
            summary = "Get latest balance data",
            description = "Get latest balance data")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Got latest balance",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BalanceResponse.class)))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<BalanceResponse> getLatestBalanceData() {
        LocalDateTime dateTime = LocalDateTime.now();
        log.info("Getting latest balance data from [{}]", dateTime);
        BalanceResponse latestBalanceData = this.balanceService.getLatestBalanceData(dateTime);
        return new ResponseEntity<>(latestBalanceData, HttpStatus.CREATED);
    }
}
