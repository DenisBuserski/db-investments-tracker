package com.investments.tracker.service;

import com.investments.tracker.model.dto.DepositDTO;
import com.investments.tracker.model.dto.DepositResultDTO;

import java.time.LocalDate;
import java.util.List;

public interface CashTransactionService {
    List<DepositResultDTO> getAllDepositsFromTo(LocalDate from, LocalDate to);

    void insertDeposit(DepositDTO depositDTO);
}
