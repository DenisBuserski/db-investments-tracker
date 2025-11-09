--liquibase formatted sql

--changeset dbuserki:005-create-portfolio-table
CREATE TABLE portfolio (
    id BIGSERIAL PRIMARY KEY,
    last_updated DATE NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INT,
    invested_money NUMERIC(18,2) NOT NULL,
    average_price NUMERIC(18,4) NOT NULL,
    dividends_amount NUMERIC(18,2) NOT NULL,
    status VARCHAR(50) NOT NULL
);
