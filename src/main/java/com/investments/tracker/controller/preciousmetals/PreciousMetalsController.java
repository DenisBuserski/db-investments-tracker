package com.investments.tracker.controller.preciousmetals;

import com.investments.tracker.service.gold.GoldService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/preciousmetals/gold")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST }
)
@Slf4j
@Tag(name = "Precious Metals Controller", description = "Contains REST methods for Precious metals interacting")
@RequiredArgsConstructor
public class PreciousMetalsController {
    private final GoldService goldService;

    @PostMapping(value = "/in", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "insertGoldTransaction",
            summary = "Insert new gold transaction in the database",
            description = "Insert new gold transaction in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gold transaction created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> insertGoldTransaction(@RequestBody @Valid GoldBuyRequest goldBuyRequest) {
        log.info("Inserting GOLD transaction");
        goldService.insertGoldTransaction(goldBuyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
