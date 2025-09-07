--liquibase formatted sql

--changeset dbuserki:005-create-portfolio-table
CREATE TABLE portfolio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    last_updated DATE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT,
    invested_money DECIMAL(18,2) NOT NULL,
    average_price DECIMAL(18,6) NOT NULL,
    dividends_amount DECIMAL(18,2) NOT NULL,
    status VARCHAR(50) NOT NULL
)