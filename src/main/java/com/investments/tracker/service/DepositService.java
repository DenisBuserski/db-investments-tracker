package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.dto.DepositResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DepositService {
    List<DepositResponseDTO> getAllDepositsFromTo(LocalDate from, LocalDate to);

    BalanceResponseDTO insertDeposit(DepositRequestDTO depositDTO);

}
