package com.investments.tracker.model.validation;

import com.investments.tracker.model.enums.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {
    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext context) {
        return currency != null && Arrays.asList(Currency.values()).contains(currency);
    }
}
