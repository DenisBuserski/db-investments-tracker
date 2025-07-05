package com.investments.tracker.service;

import com.investments.tracker.controller.response.BalanceResponse;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.model.Transaction;
import com.investments.tracker.repository.BalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.investments.tracker.controller.response.BalanceResponse.createBalanceResponse;
import static com.investments.tracker.enums.CashTransactionType.*;

@Service
@Slf4j
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public BalanceResponse getLatestBalanceData(LocalDateTime dateTime) {
        Optional<Balance> latestBalance = this.balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            return createBalanceResponse(latestBalance.get());
        }
        log.info("No balance found for [{}]", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return createBalanceResponse(null);
    }


}
