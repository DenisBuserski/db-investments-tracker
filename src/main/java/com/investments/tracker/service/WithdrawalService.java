package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalRequestDTO;
import com.investments.tracker.model.dto.withdraw.WithdrawalResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface WithdrawalService {
    BalanceResponseDTO withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO);

    List<WithdrawalResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to);

    BigDecimal getTotalWithdrawalsAmount();
}
