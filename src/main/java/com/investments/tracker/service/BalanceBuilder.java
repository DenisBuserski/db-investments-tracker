package com.investments.tracker.service;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;

public interface BalanceBuilder {
    Balance createNewBalanceFromCashTransaction(Balance balance, CashTransaction dividend);
}
