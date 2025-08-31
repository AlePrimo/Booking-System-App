-- ==========================
-- Usuarios iniciales
-- ==========================
INSERT INTO users (id, name, email, password) VALUES
(1, 'Admin User', 'admin@booking.com', 'admin123'), -- password: admin123
(2, 'Maria Lopez', 'maria@provider.com', 'provider123'), -- password: provider123
(3, 'Juan Perez', 'juan@customer.com', 'customer123'), -- password: customer123
(4, 'Ana Torres', 'ana@customer.com', 'customer456'); -- password: customer123

-- Roles de usuarios
INSERT INTO user_roles (user_id, role) VALUES
(1, 'ADMIN'),
(2, 'PROVIDER'),
(3, 'CUSTOMER'),
(4, 'CUSTOMER');

-- ==========================
-- Servicios (Offerings)
-- ==========================
INSERT INTO offerings (id, name, description, duration_minutes, price, provider_id) VALUES
(1, 'Corte de Pelo', 'Corte de pelo profesional para hombre o mujer', 45, 1500.00, 2),
(2, 'Peinado de Fiesta', 'Peinado especial para eventos y fiestas', 60, 3000.00, 2),
(3, 'Coloración Capilar', 'Coloración completa del cabello', 120, 6000.00, 2);

-- ==========================
-- Reservas (Bookings)
-- ==========================
INSERT INTO bookings (id, booking_date_time, status, customer_id, offering_id) VALUES
(1, '2025-09-01 10:00:00', 'CONFIRMED', 3, 1), -- Juan reserva corte
(2, '2025-09-02 15:00:00', 'PENDING', 4, 2),   -- Ana reserva peinado
(3, '2025-09-05 11:00:00', 'CANCELLED', 3, 3); -- Juan cancela coloración

-- ==========================
-- Pagos (Payments)
-- ==========================
INSERT INTO payments (id, amount, method, status, booking_id) VALUES
(1, 1500.00, 'CASH', 'PAID', 1),
(2, 3000.00, 'CREDIT_CARD', 'PENDING', 2),
(3, 6000.00, 'PAYPAL', 'FAILED', 3);

-- ==========================
-- Notificaciones (Notifications)
-- ==========================
INSERT INTO notifications (id, type, message, sent, user_id) VALUES
(1, 'EMAIL', 'Tu reserva #1 ha sido confirmada.', true, 3),
(2, 'SMS', 'Recordatorio: reserva #2 pendiente de pago.', false, 4),
(3, 'EMAIL', 'Tu reserva #3 fue cancelada.', true, 3);
