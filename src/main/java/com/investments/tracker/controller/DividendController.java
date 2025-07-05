package com.investments.tracker.controller;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.controller.request.DividendRequest;
import com.investments.tracker.service.dividend.DividendService;
import io.swagger.v3.oas.annotations.Operation;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/dividends")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = RequestMethod.GET
)
@Slf4j
@Tag(name = "Dividend Controller", description = "REST endpoint for retrieving the latest balance data")
public class DividendController {
    private final DividendService dividendService;

    @Autowired
    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @PostMapping(value = "/in", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
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
    public ResponseEntity<BalanceResponse> insertDividend(@RequestBody @Valid DividendRequest dividendRequest) {
        log.info("Inserting dividend for [{}]", dividendRequest.getProductName());
        BalanceResponse balanceResponse = this.dividendService.insertDividend(dividendRequest);
        return new ResponseEntity<>(balanceResponse, HttpStatus.CREATED);
    }
}
