package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.dto.DepositRequestDTO;
import com.investments.tracker.model.dto.DepositResultDTO;
import com.investments.tracker.model.dto.WithdrawalRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface CashTransactionService {
    List<DepositResultDTO> getAllDepositsFromTo(LocalDate from, LocalDate to);

    void insertDeposit(DepositRequestDTO depositDTO);

    Balance withdrawCash(WithdrawalRequestDTO withdrawalRequestDTO);
}
