package com.investments.tracker.controller.report;

import com.investments.tracker.service.report.ReportService;
import com.investments.tracker.service.report.WeeklyProductPositionDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
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

    @PostMapping(value = "/prepare", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WeeklyProductPositionDTO>> prepareWeeklyViewReport(
            @Parameter(description = "The last day of the week", required = true) @RequestParam("lastDayOfTheWeek") LocalDate lastDayOfTheWeek) {
        int weekNumber = lastDayOfTheWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
        LocalDate firstDayOfWeek = lastDayOfTheWeek.minusDays(6); // If the last day of the week is in the new year
        log.info("Preparing weekly view report for week {} of year {}", weekNumber, firstDayOfWeek.getYear());
        List<WeeklyProductPositionDTO> weeklyProductPositions = reportService.prepareWeeklyViewReport(lastDayOfTheWeek);
        return ResponseEntity.status(HttpStatus.CREATED).body(weeklyProductPositions);
    }

    @PostMapping(value = "/generate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<WeeklyViewResponse> generateWeeklyViewReport(@RequestBody List<WeeklyProductPositionDTO> updatedWeeklyProductPositions) {
        log.info("Generating weekly view report");
        WeeklyViewResponse weeklyViewResponse = reportService.generateWeeklyViewReport(updatedWeeklyProductPositions);
        return ResponseEntity.status(HttpStatus.CREATED).body(weeklyViewResponse);
    }

}
