package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.dividend.DividendRequestDTO;
import jakarta.validation.Valid;

public interface DividendService {
    BalanceResponseDTO insertDividend(DividendRequestDTO dividendRequestDTO);
}
