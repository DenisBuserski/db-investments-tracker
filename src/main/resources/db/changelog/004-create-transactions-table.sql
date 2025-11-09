--liquibase formatted sql

--changeset dbuserki:004-create-transactions-table
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    single_price NUMERIC(18,3) NOT NULL,
    quantity INT,
    exchange_rate NUMERIC(18,4) NOT NULL,
    total_amount NUMERIC(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    base_product_currency VARCHAR(3) NOT NULL,
    description VARCHAR(255)
);
