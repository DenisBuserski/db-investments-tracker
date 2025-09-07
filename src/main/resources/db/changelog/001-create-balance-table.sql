--liquibase formatted sql

--changeset dbuserski:001-create-balance-table
CREATE TABLE balance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    balance DECIMAL(18,2) NOT NULL, -- 18 total digits, 2 after the decimal
    total_investments DECIMAL(18,2) NOT NULL,
    total_deposits DECIMAL(18,2) NOT NULL,
    total_withdrawals DECIMAL(18,2) NOT NULL,
    total_dividends DECIMAL(18,2) NOT NULL,
    total_fees DECIMAL(18,2) NOT NULL,
    last_portfolio_value DECIMAL(18,2) NOT NULL,
    last_unrealized_pl DECIMAL(18,2) NOT NULL,
    last_unrealized_pl_percentage DECIMAL(18,2) NOT NULL,
    total_sold DECIMAL(18,2) NOT NULL,
    realized_pl DECIMAL(18,2) NOT NULL
)