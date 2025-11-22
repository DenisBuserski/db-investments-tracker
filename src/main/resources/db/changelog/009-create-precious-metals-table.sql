-- liquibase formatted sql

-- changeset dbuserski:009-create-precious-metals-table
CREATE TABLE precious_metals (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    seller_name VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    url TEXT,
    transaction_date DATE NOT NULL,
    size_in_grams DOUBLE PRECISION,
    price_bgn NUMERIC(18,2) NOT NULL,
    price_eur NUMERIC(18,2) NOT NULL,
    price_per_gram_eur NUMERIC(18,2) NOT NULL,
    price_per_gram_on_date_eur NUMERIC(18,2) NOT NULL,
    difference NUMERIC(18,2) NOT NULL
);
