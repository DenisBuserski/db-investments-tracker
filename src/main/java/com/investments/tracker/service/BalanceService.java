package com.investments.tracker.service;

import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.investments.tracker.controller.balance.BalanceResponse.createBalanceResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceResponse getLatestBalanceData(LocalDateTime dateTime) {
        Optional<Balance> latestBalance = this.balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            return createBalanceResponse(latestBalance.get());
        }
        log.info("No balance found for [{}]", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return createBalanceResponse(null);
    }


}
