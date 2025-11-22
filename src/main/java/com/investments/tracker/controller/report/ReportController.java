package com.investments.tracker.controller.report;

import com.investments.tracker.service.report.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/report")
@CrossOrigin(
        origins = "http://localhost:3000",
        methods = { RequestMethod.POST }
)
@Slf4j
@Tag(name = "Report Controller", description = "Provides different reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // Trigger calculation via API
    // List of WeeklyPositions
    // Calculate Total invested money
    // Calculate Total current value
    // Total Unrealized P/L
    // Total Unrealized P/L %

    @PostMapping(value = "/prepare", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WeeklyViewResponse> prepareWeeklyViewReport(@RequestBody DateRangeRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        log.info("Preparing weekly view report for {} to {}", startDate, endDate);
        WeeklyViewResponse weeklyViewResponse = reportService.prepareWeeklyViewReport(startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(weeklyViewResponse);
    }

    @PostMapping(value = "/generate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateWeeklyViewReport(@RequestBody WeeklyViewResponse updatedResponse) {
        log.info("Generating weekly view report");
        reportService.generateWeeklyViewReport(updatedResponse);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
