package com.investments.tracker.enums;

public enum CashTransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    DIVIDEND("DIVIDEND"),
    FEE("FEE"),
    TRANSACTION_EXECUTION_FEE("TRANSACTION_EXECUTION_FEE"),
    ECF_2022("EXCHANGE_CONNECTION_FEE_2022"),
    ECF_2023("EXCHANGE_CONNECTION_FEE_2023"),
    ECF_2024("EXCHANGE_CONNECTION_FEE_2024"),
    ECF_2025("EXCHANGE_CONNECTION_FEE_2025");

    private final String name;

    CashTransactionType(String name) {
        this.name = name;
    }
}
