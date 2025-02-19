package com.investments.tracker.model.enums;

public enum Currency {
    EUR("EUR"),
    USD("USD");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public static Currency fromString(String value) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equalsIgnoreCase(value)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unsupported currency: " + value);
    }
}
