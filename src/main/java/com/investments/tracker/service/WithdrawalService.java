package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;


public interface WithdrawalService {
    BalanceResponseDTO withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO);
}
