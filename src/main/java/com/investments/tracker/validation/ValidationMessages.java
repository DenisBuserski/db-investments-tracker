package com.investments.tracker.validation;

public class ValidationMessages {
    public static final String DATE_NOT_NULL = "Date cannot be NULL!";
    public static final String DATE_NOT_IN_FUTURE = "Date cannot be in the future!";
    public static final String CURRENCY_NOT_NULL = "Currency cannot be NULL!";
    public static final String DESCRIPTION_NOT_NULL = "Description cannot be NULL!";
    public static final String QUANTITY_MORE_THAN_ZERO = "Quantity must be more than 0!";
    public static final String EXCHANGE_RATE_NOT_NULL = "Exchange rate cannot be NULL!";

    // Deposit
    public static final String DEPOSIT_AMOUNT_NOT_NULL = "Deposit amount cannot be NULL!";
    public static final String DEPOSIT_AMOUNT_MORE_THAN_ZERO = "Deposit amount must be more than 0!";

    // Withdrawal
    public static final String WITHDRAWAL_AMOUNT_NOT_NULL = "Withdrawal amount cannot be NULL!";
    public static final String WITHDRAWAL_AMOUNT_MORE_THAN_ZERO = "Withdrawal amount must be more than 0!";
    public static final String WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE = "Withdrawal date cannot be before the latest balance date!";
    public static final String WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST = "Withdrawal cannot be made, because no balance exists!";
    public static final String NOT_ENOUGH_TO_WITHDRAWAL = "You don't have enough money to withdraw. Current balance is [%1 %2]";

    // Transaction
    public static final String TRANSACTION_TYPE_NOT_NULL = "Transaction type cannot be NULL!";
    public static final String TRANSACTION_PRICE_NOT_NULL = "Transaction price cannot be NULL!";
    public static final String TRANSACTION_PRICE_MORE_THAN_ZERO = "Transaction price must be more than 0!";
    public static final String TRANSACTION_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST = "Transaction cannot be created, because no balance exists!";
    public static final String TRANSACTION_NOT_POSSIBLE_NOT_ENOUGH_MONEY = "Transaction of type [%1] cannot be created because there is not enough money";

    // Product
    public static final String PRODUCT_TYPE_NOT_NULL = "Product type cannot be NULL!";
    public static final String PRODUCT_NAME_NOT_BLANK_OR_NULL = "Product name cannot be blank or NULL!";
    public static final String PRODUCT_NOT_EXIST = "Product with name %1 not found!";


    // Fee
    public static final String FEE_TYPE_NOT_NULL = "Fee type cannot be NULL!";
    public static final String FEE_AMOUNT_NOT_NULL = "Fee amount cannot be NULL!";

    // Dividend
    public static final String DIVIDEND_AMOUNT_NOT_NULL = "Dividend amount cannot be NULL!";
    public static final String DIVIDEND_AMOUNT_MORE_THAN_ZERO = "Dividend amount must be more than 0!";
    public static final String DIVIDEND_TAX_AMOUNT_NOT_NULL = "Dividend tax amount cannot be NULL!";
    public static final String DIVIDEND_TAX_AMOUNT_MORE_THAN_ZERO = "Dividend tax amount cannot be in the future!";
}
