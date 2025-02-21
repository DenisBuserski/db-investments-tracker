package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;

import java.time.LocalDate;
import java.util.List;


public interface WithdrawalService {
    BalanceResponseDTO withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO);

    List<DepositResponseDTO> getAllWithdrawalsFromTo(LocalDate from, LocalDate to);
}
