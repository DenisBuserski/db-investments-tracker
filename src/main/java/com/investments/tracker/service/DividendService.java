package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import com.investments.tracker.model.dto.dividend.DividendResponseDTO;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DividendService {
    BalanceResponseDTO insertDividend(DividendRequestDTO dividendRequestDTO);

    List<DividendResponseDTO> getAllDividendsFromTo(LocalDate from, LocalDate to);

    BigDecimal getTotalDividendsAmount();
}
