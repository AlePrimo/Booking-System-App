-- =================================
-- V1__init.sql
-- Migración inicial Booking System
-- =================================

-- Tabla de usuarios
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabla de roles asignados a usuarios (Role como enum)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY(user_id, role)
);

-- Tabla de offerings (antes Service)
CREATE TABLE offerings (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    duration_minutes INT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    provider_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de bookings
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_date_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    customer_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    offering_id BIGINT NOT NULL REFERENCES offerings(id) ON DELETE CASCADE
);

-- Tabla de pagos
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(10,2) NOT NULL,
    method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id) ON DELETE CASCADE
);

-- Tabla de notificaciones
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    message VARCHAR(500) NOT NULL,
    sent BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

