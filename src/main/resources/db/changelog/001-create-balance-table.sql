--liquibase formatted sql

--changeset dbuserski:001-create-balance-table
CREATE TABLE balance (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    balance NUMERIC(18,2) NOT NULL, -- 18 total digits, 2 after the decimal
    total_investments NUMERIC(18,2) NOT NULL,
    total_deposits NUMERIC(18,2) NOT NULL,
    total_withdrawals NUMERIC(18,2) NOT NULL,
    total_dividends NUMERIC(18,2) NOT NULL,
    total_fees NUMERIC(18,2) NOT NULL,
    last_portfolio_value NUMERIC(18,2) NOT NULL,
    last_unrealized_pl NUMERIC(18,2) NOT NULL,
    last_unrealized_pl_percentage NUMERIC(18,2) NOT NULL,
    total_sold NUMERIC(18,2) NOT NULL,
    realized_pl NUMERIC(18,2) NOT NULL
);
