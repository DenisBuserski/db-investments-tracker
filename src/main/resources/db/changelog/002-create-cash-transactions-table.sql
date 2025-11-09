--liquibase formatted sql

--changeset dbuserki:002-create-cash-transactions-table
CREATE TABLE cash_transactions (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    cash_transaction_type VARCHAR(50) NOT NULL,
    amount NUMERIC(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(255),
    reference_id BIGINT
);
