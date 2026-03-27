CREATE SCHEMA IF NOT EXISTS product_service;
CREATE SCHEMA IF NOT EXISTS order_service;
CREATE SCHEMA IF NOT EXISTS inventory_service;
CREATE SCHEMA IF NOT EXISTS payment_service;

-- Product Service tables
CREATE TABLE IF NOT EXISTS product_service.products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order Service tables
CREATE TABLE IF NOT EXISTS order_service.orders (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_service.order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES order_service.orders(id),
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL
);

-- Inventory Service tables
CREATE TABLE IF NOT EXISTS inventory_service.inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0
);

-- Payment Service tables
CREATE TABLE IF NOT EXISTS payment_service.payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed product data
INSERT INTO product_service.products (name, description, price) VALUES
    ('Laptop', 'High-performance laptop', 999.99),
    ('Mouse', 'Wireless ergonomic mouse', 29.99),
    ('Keyboard', 'Mechanical keyboard', 79.99),
    ('Monitor', '27-inch 4K display', 449.99),
    ('Headphones', 'Noise-cancelling headphones', 199.99)
ON CONFLICT DO NOTHING;

-- Seed inventory data
INSERT INTO inventory_service.inventory (product_id, quantity, reserved_quantity) VALUES
    (1, 50, 0),
    (2, 200, 0),
    (3, 100, 0),
    (4, 30, 0),
    (5, 75, 0)
ON CONFLICT (product_id) DO NOTHING;
