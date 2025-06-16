package com.investments.tracker.enums;

public enum CashTransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    DIVIDEND("DIVIDEND"),
    FEE("FEE");

    private final String name;

    CashTransactionType(String name) {
        this.name = name;
    }
}
