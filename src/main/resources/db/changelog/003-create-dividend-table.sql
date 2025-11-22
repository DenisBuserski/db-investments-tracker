--liquibase formatted sql

--changeset dbuserki:003-create-dividends-table
CREATE TABLE dividends (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT,
    total_amount_after_tax_and_conversion NUMERIC(18,2) NOT NULL,
    total_amount_after_tax_before_conversion NUMERIC(18,2) NOT NULL,
    total_base_amount NUMERIC(18,2) NOT NULL,
    total_base_tax_amount NUMERIC(18,2) NOT NULL,
    amount_per_share NUMERIC(18,4) NOT NULL,
    tax_amount_per_share NUMERIC(18,4) NOT NULL,
    exchange_rate NUMERIC(18,4) NOT NULL,
    dividend_currency VARCHAR(3) NOT NULL
);
