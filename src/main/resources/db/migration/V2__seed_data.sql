-- ==========================
-- Usuarios iniciales
-- ==========================
INSERT INTO users (name, email, password) VALUES
('Admin User', 'admin@booking.com', 'admin123'), -- id = 1
('Maria Lopez', 'maria@provider.com', 'provider123'), -- id = 2
('Juan Perez', 'juan@customer.com', 'customer123'), -- id = 3
('Ana Torres', 'ana@customer.com', 'customer456'); -- id = 4

-- Roles de usuarios
INSERT INTO user_roles (user_id, role) VALUES
(1, 'ADMIN'),
(2, 'PROVIDER'),
(3, 'CUSTOMER'),
(4, 'CUSTOMER');

-- ==========================
-- Servicios (Offerings)
-- ==========================
INSERT INTO offerings (name, description, duration_minutes, price, provider_id) VALUES
('Corte de Pelo', 'Corte de pelo profesional para hombre o mujer', 45, 1500.00, 2),
('Peinado de Fiesta', 'Peinado especial para eventos y fiestas', 60, 3000.00, 2),
('Coloración Capilar', 'Coloración completa del cabello', 120, 6000.00, 2);

-- ==========================
-- Reservas (Bookings)
-- ==========================
INSERT INTO bookings (booking_date_time, status, customer_id, offering_id) VALUES
('2025-09-01 10:00:00', 'CONFIRMED', 3, 1), -- Juan reserva corte
('2025-09-02 15:00:00', 'PENDING', 4, 2),   -- Ana reserva peinado
('2025-09-05 11:00:00', 'CANCELLED', 3, 3); -- Juan cancela coloración

-- ==========================
-- Pagos (Payments)
-- ==========================
INSERT INTO payments (amount, method, status, booking_id) VALUES
(1500.00, 'CASH', 'PAID', 1),
(3000.00, 'CREDIT_CARD', 'PENDING', 2),
(6000.00, 'PAYPAL', 'FAILED', 3);

-- ==========================
-- Notificaciones (Notifications)
-- ==========================
INSERT INTO notifications (type, message, sent, user_id) VALUES
('EMAIL', 'Tu reserva #1 ha sido confirmada.', true, 3),
('SMS', 'Recordatorio: reserva #2 pendiente de pago.', false, 4),
('EMAIL', 'Tu reserva #3 fue cancelada.', true, 3);
