package com.investments.tracker.service;

import com.investments.tracker.model.dto.BalanceResponseDTO;
import com.investments.tracker.model.dto.deposit.DepositRequestDTO;
import com.investments.tracker.model.dto.deposit.DepositResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DepositService {
    BalanceResponseDTO insertDeposit(DepositRequestDTO depositDTO);

    List<DepositResponseDTO> getAllDepositsFromTo(LocalDate from, LocalDate to);

    BigDecimal getTotalDepositsAmount();

}
