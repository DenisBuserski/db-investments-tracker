package com.investments.tracker.service.report;

import com.investments.tracker.controller.report.WeeklyViewResponse;
import com.investments.tracker.model.WeeklyOverview;
import com.investments.tracker.repository.TransactionRepository;
import com.investments.tracker.repository.WeeklyOverviewRepository;
import com.investments.tracker.repository.WeeklyPositionRepository;
import com.investments.tracker.service.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final WeeklyPositionRepository weeklyPositionRepository;
    private final WeeklyOverviewRepository weeklyOverviewRepository;
    private final EmailService emailService;

    BigDecimal totalBeggingPortfolioValue = BigDecimal.ZERO;
    BigDecimal totalInvestedValue = BigDecimal.ZERO;
    BigDecimal totalUnrealizedProfitLoss = BigDecimal.ZERO;
    BigDecimal totalUnrealizedProfitLossPercentage = BigDecimal.ZERO; // Maybe calcualte this

    @Transactional
    public WeeklyViewResponse prepareWeeklyViewReport(LocalDate startDate, LocalDate endDate) {
        int weekNumber = startDate.get(WeekFields.ISO.weekOfWeekBasedYear());

        List<WeeklyProductPosition> weeklyPositions = calculateWeeklyPositions();
        log.info("Finished calculating weekly positions - {}", weeklyPositions.size());

        return WeeklyViewResponse.builder()
                .weekNumber(weekNumber)
                .startDate(startDate)
                .endDate(endDate)
                .returnOnInvestment(totalUnrealizedProfitLoss.divide(totalBeggingPortfolioValue))
                .beggingPortfolioValue(totalBeggingPortfolioValue)
                .totalInvestedValue(totalInvestedValue)
                .totalUnrealizedProfitLoss(totalUnrealizedProfitLoss)
                .totalUnrealizedProfitLossPercentage(totalUnrealizedProfitLossPercentage)
                .weeklyPositions(weeklyPositions)
                .build();

    }

    private List<WeeklyProductPosition> calculateWeeklyPositions() {
        log.info("Getting unique product names with their total quantity and invested money");
        return transactionRepository.findDistinctProductDetails()
                       .stream()
                       .map(productDetails -> {
                           totalBeggingPortfolioValue.add(productDetails.getTotalInvestedValue());
                           totalInvestedValue.add(null);
                           totalUnrealizedProfitLoss.add(null);

                           return WeeklyProductPosition.builder()
                                   .productName(productDetails.getProductName())
                                   .quantity(Math.toIntExact(productDetails.getTotalQuantity()))
                                   .beggingPortfolioValue(productDetails.getTotalInvestedValue())
                                   .openPrice(null)
                                   .currency(productDetails.getCurrency())
                                   .exchangeRate(null)
                                   .totalValue(null) // openPrice * quantity
                                   .totalUnrealizedProfitLoss(null) // totalValue - beggingPortfolioValue
                                   .totalUnrealizedProfitLossPercentage(null) // (totalValue / beggingPortfolioValue) / beggingPortfolioValue
                                   .build();

                       })
                .collect(Collectors.toList());
    }

    @Transactional
    public void generateWeeklyViewReport(WeeklyViewResponse updatedResponse) {
        // Save the WeeklyPosition
        List<WeeklyProductPosition> weeklyPositions = updatedResponse.getWeeklyPositions();

        // calculate totals before saving in db

        log.info("Inserting entries in table 'weekly_overview' for the period {} to {}", updatedResponse.getStartDate(), updatedResponse.getEndDate());
        weeklyOverviewRepository.save(weeklyOverviewBuilder(updatedResponse));


        // Take from updatedResponse and save in DB
        // startDate / endDate / returnOnInvestment / beggingPortfolioValue / totalInvestedValue / totalUnrealizedProfitLoss / totalUnrealizedProfitLossPercentage




        log.info("Sending email for Weekly view report");
        // emailService.sendEmail();
    }

    private WeeklyOverview weeklyOverviewBuilder(WeeklyViewResponse updatedResponse) {
        return WeeklyOverview.builder()
                .startDate(updatedResponse.getStartDate())
                .endDate(updatedResponse.getEndDate())
                .returnOnInvestment(updatedResponse.getReturnOnInvestment())
                .beggingPortfolioValue(updatedResponse.getBeggingPortfolioValue())
                .totalInvestedValue(updatedResponse.getTotalInvestedValue())
                .totalUnrealizedProfitLoss(updatedResponse.getTotalUnrealizedProfitLoss())
                .totalUnrealizedProfitLossPercentage(updatedResponse.getTotalUnrealizedProfitLossPercentage())
                .build();
    }
}
