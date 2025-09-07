--liquibase formatted sql

--changeset dbuserki:003-create-dividends-table
CREATE TABLE dividends (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT,
    dividend_amount_after_tax DECIMAL(18,2) NOT NULL,
    dividend_amount_before_tax DECIMAL(18,2) NOT NULL,
    dividend_tax_amount DECIMAL(18,2) NOT NULL,
    dividend_amount_per_share DECIMAL(18,4) NOT NULL,
    dividend_tax_amount_per_share DECIMAL(18,4) NOT NULL,
    exchange_rate DECIMAL(18,4) NOT NULL,
    dividend_currency VARCHAR(3) NOT NULL
)