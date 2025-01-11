CREATE TABLE IF NOT EXISTS ORDER_TB(
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    prod_count BIGINT,
    product_name VARCHAR(255),
    address VARCHAR(255),
    status VARCHAR(255)
);