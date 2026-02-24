CREATE USER bank_card_user WITH PASSWORD 'password';
CREATE SCHEMA IF NOT EXISTS bank_card_schema AUTHORIZATION bank_card_user;
ALTER ROLE bank_card_user SET search_path TO bank_card_schema;
