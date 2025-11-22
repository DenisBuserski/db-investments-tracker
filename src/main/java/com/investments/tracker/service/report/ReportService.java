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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final TransactionRepository transactionRepository;
    private final WeeklyPositionRepository weeklyPositionRepository;
    private final WeeklyOverviewRepository weeklyOverviewRepository;
    private final EmailService emailService;

    @Transactional
    public List<WeeklyProductPositionDTO> prepareWeeklyViewReport(LocalDate lastDayOfTheWeek) {
        log.info("Getting unique product names with their total quantity and invested money");
        return transactionRepository
                .findDistinctProductDetails(lastDayOfTheWeek)
                .stream()
                .map(productDetails -> WeeklyProductPositionDTO.builder()
                        .date(lastDayOfTheWeek)
                        .productName(productDetails.getProductName())
                        .quantity(Math.toIntExact(productDetails.getTotalQuantity()))
                        .beggingPortfolioValue(productDetails.getTotalInvestedValue())
                        .openPrice(null) // Passed by the user
                        .currency(productDetails.getBaseProductCurrency())
                        .exchangeRate(null) // Passed by the user
                        .totalValue(null) // openPrice * quantity
                        .totalUnrealizedProfitLoss(null) // totalValue - beggingPortfolioValue
                        .totalUnrealizedProfitLossPercentage(null) // (totalValue / beggingPortfolioValue) / beggingPortfolioValue
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public WeeklyViewResponse generateWeeklyViewReport(List<WeeklyProductPositionDTO> updatedWeeklyProductPositions) {
        LocalDate lastDayOfTheWeek = updatedWeeklyProductPositions.getFirst().getDate();
        LocalDate firstDayOfWeek = lastDayOfTheWeek.minusDays(6);
        int weekNumber = lastDayOfTheWeek.get(WeekFields.ISO.weekOfWeekBasedYear());

        BigDecimal totalBeggingPortfolioValue = BigDecimal.ZERO;
        BigDecimal totalInvestedValue = BigDecimal.ZERO;
        BigDecimal totalUnrealizedProfitLoss = BigDecimal.ZERO;

        List<WeeklyProductPositionDTO> finalWeeklyProductPositions = new ArrayList<>();

        for (WeeklyProductPositionDTO updatedWeeklyProductPosition : updatedWeeklyProductPositions) {
            BigDecimal openPrice;
            BigDecimal exchangeRate;
            BigDecimal beggingPortfolioValueForProduct = updatedWeeklyProductPosition.getBeggingPortfolioValue();
            totalBeggingPortfolioValue.add(beggingPortfolioValueForProduct);

            if (BigDecimal.ZERO.compareTo(updatedWeeklyProductPosition.getExchangeRate()) == 0) {
                exchangeRate = updatedWeeklyProductPosition.getExchangeRate();
                openPrice = BigDecimal.ONE;
                // Calculate open price with rate
            } else {
                exchangeRate = BigDecimal.ZERO;
                openPrice = updatedWeeklyProductPosition.getOpenPrice();
            }

            BigDecimal investedValue = openPrice.multiply(BigDecimal.valueOf(updatedWeeklyProductPosition.getQuantity()));
            totalInvestedValue.add(investedValue);

            BigDecimal unrealizedProfitLoss = totalInvestedValue.subtract(beggingPortfolioValueForProduct);
            totalUnrealizedProfitLoss.add(unrealizedProfitLoss);

            BigDecimal unrealizedProfitLossPercentage = unrealizedProfitLoss.divide(beggingPortfolioValueForProduct);

            WeeklyProductPositionDTO newWeeklyProductPosition = WeeklyProductPositionDTO.builder()
                    .date(lastDayOfTheWeek)
                    .productName(updatedWeeklyProductPosition.getProductName())
                    .quantity(Math.toIntExact(updatedWeeklyProductPosition.getQuantity()))
                    .beggingPortfolioValue(beggingPortfolioValueForProduct)
                    .openPrice(openPrice)
                    .currency(updatedWeeklyProductPosition.getCurrency())
                    .exchangeRate(exchangeRate)
                    .totalValue(investedValue)
                    .totalUnrealizedProfitLoss(unrealizedProfitLoss)
                    .totalUnrealizedProfitLossPercentage(unrealizedProfitLossPercentage)
                    .build();
            finalWeeklyProductPositions.add(newWeeklyProductPosition);
            // Save in DB
        }

        BigDecimal totalUnrealizedProfitLossPercentage = totalUnrealizedProfitLoss.divide(totalBeggingPortfolioValue);
        BigDecimal totalROIPercentage = totalUnrealizedProfitLoss.divide(totalBeggingPortfolioValue);

        log.info("Inserting entries in table 'weekly_overview' for week {} of year {}", weekNumber, firstDayOfWeek.getYear());
        weeklyOverviewRepository.save(weeklyOverviewBuilder(lastDayOfTheWeek, weekNumber,
                totalROIPercentage, totalBeggingPortfolioValue, totalInvestedValue, totalUnrealizedProfitLoss, totalUnrealizedProfitLossPercentage));

        WeeklyViewResponse weeklyViewResponse = WeeklyViewResponse.builder()
                .weekNumber(weekNumber)
                .lastDayOfTheWeek(lastDayOfTheWeek)
                .returnOnInvestment(totalROIPercentage)
                .beggingPortfolioValue(totalBeggingPortfolioValue)
                .totalInvestedValue(totalInvestedValue)
                .totalUnrealizedProfitLoss(totalUnrealizedProfitLoss)
                .totalUnrealizedProfitLossPercentage(totalUnrealizedProfitLossPercentage)
                .weeklyPositions(finalWeeklyProductPositions)
                .build();

        log.info("Sending email for Weekly view report");
        String subject = "Weekly Overview Report";
        emailService.sendEmailForWeeklyView(subject, weeklyViewResponse, true);

        return weeklyViewResponse;
    }

    private WeeklyOverview weeklyOverviewBuilder(LocalDate lastDayOfTheWeek, int weekNumber,
                                                 BigDecimal totalROIPercentage,
                                                 BigDecimal totalBeggingPortfolioValue,
                                                 BigDecimal totalInvestedValue,
                                                 BigDecimal totalUnrealizedProfitLoss,
                                                 BigDecimal totalUnrealizedProfitLossPercentage) {
        return WeeklyOverview.builder()
                .lastDayOfTheWeek(lastDayOfTheWeek)
                .week(weekNumber)
                .ROIPercentage(totalROIPercentage)
                .beggingPortfolioValue(totalBeggingPortfolioValue)
                .totalInvestedValue(totalInvestedValue)
                .totalUnrealizedProfitLoss(totalUnrealizedProfitLoss)
                .totalUnrealizedProfitLossPercentage(totalUnrealizedProfitLossPercentage)
                .build();
    }
}
