package com.investments.tracker.model.enums;

public enum CashTransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    DIVIDEND("DIVIDEND"),
    TRANSACTION_EXECUTION_FEE("TRANSACTION_EXECUTION_FEE");

    private final String name;

    CashTransactionType(String name) {
        this.name = name;
    }
}
