package com.investments.tracker.controller.report;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.investments.tracker.validation.ValidationMessages.DATE_NOT_IN_FUTURE;
import static com.investments.tracker.validation.ValidationMessages.DATE_NOT_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DateRangeRequest {
    @NotNull(message = DATE_NOT_NULL)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    private LocalDate startDate;

    @NotNull(message = DATE_NOT_NULL)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    private LocalDate endDate;
}
