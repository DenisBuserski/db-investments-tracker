package com.investments.tracker.service;

import com.investments.tracker.model.dto.WithdrawalRequestDTO;

import java.math.BigDecimal;

public interface WithdrawalService {
    BigDecimal withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO);
}
