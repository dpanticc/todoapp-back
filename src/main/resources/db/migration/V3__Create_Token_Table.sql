-- V3__Create_Token_Table.sql
CREATE TABLE IF NOT EXISTS token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT, -- Use BIGINT data type to match the users table
    FOREIGN KEY (user_id) REFERENCES users(id) -- Match the referenced table name and column name
);