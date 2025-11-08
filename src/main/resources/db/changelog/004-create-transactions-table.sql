--liquibase formatted sql

--changeset dbuserki:004-create-transactions-table
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    `date` DATE NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    single_price DECIMAL(18,3) NOT NULL,
    quantity INT,
    exchange_rate DECIMAL(18,4) NOT NULL,
    total_amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    base_product_currency VARCHAR(3) NOT NULL,
    description VARCHAR(255)
)