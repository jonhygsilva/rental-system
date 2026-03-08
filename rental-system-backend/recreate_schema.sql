-- recreate_schema.sql
-- WARNING: this will permanently drop all objects in the public schema.
-- Run only in local dev environments where data can be lost.

-- 1) Drop and recreate public schema (clean slate)
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

-- 2) Create tables with explicit types matching JPA entities

-- Users
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

-- Customers
CREATE TABLE customers (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  document VARCHAR(100) NOT NULL,
  phone VARCHAR(50),
  user_id BIGINT NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL,
  CONSTRAINT fk_customers_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX idx_customers_user_id ON customers(user_id);

-- Addresses
CREATE TABLE addresses (
  id BIGSERIAL PRIMARY KEY,
  street VARCHAR(255),
  number VARCHAR(50),
  complement VARCHAR(255),
  neighborhood VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(50),
  zip_code VARCHAR(50),
  customer_id BIGINT,
  CONSTRAINT fk_addresses_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);
CREATE INDEX idx_addresses_customer_id ON addresses(customer_id);

-- Equipment (singular table name used in JPA)
CREATE TABLE equipment (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(100),
  status VARCHAR(50),
  latitude DOUBLE PRECISION,
  longitude DOUBLE PRECISION,
  daily_rate NUMERIC(12,2) DEFAULT 0,
  user_id BIGINT NOT NULL,
  CONSTRAINT fk_equipment_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX idx_equipment_user_id ON equipment(user_id);

-- Rentals
CREATE TABLE rentals (
  id BIGSERIAL PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  equipment_id BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  total NUMERIC(12,2) DEFAULT 0,
  paid BOOLEAN DEFAULT false,
  status VARCHAR(50) DEFAULT 'ACTIVE',
  CONSTRAINT fk_rentals_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
  CONSTRAINT fk_rentals_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id),
  CONSTRAINT fk_rentals_address FOREIGN KEY (address_id) REFERENCES addresses(id),
  CONSTRAINT fk_rentals_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX idx_rentals_customer_id ON rentals(customer_id);
CREATE INDEX idx_rentals_equipment_id ON rentals(equipment_id);

-- 3) Verify column types (optional queries you can run after executing this file)
-- SELECT column_name, data_type, udt_name FROM information_schema.columns WHERE table_name = 'customers' ORDER BY ordinal_position;
-- SELECT count(*) FROM users; -- should be 0 until DataInitializer runs

-- End of recreate_schema.sql

