-- ==========================
-- Usuarios iniciales
-- ==========================
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@booking.com', '$2b$12$iApLtIsDE1iIHR9e5IR38ej.ZnA/uF8PdX3rPDkfIebjKQvkytTo.', 'ROLE_ADMIN'),       -- admin123
('Maria Lopez', 'maria@provider.com', '$2b$12$xK41kibOS0eZ3E3EBVbKruPhv5bgICirww/uVjdVIiyJpu7b3DEgW', 'ROLE_PROVIDER'), -- provider123
('Juan Perez', 'juan@customer.com', '$2b$12$L8/DErdaU1/6Q7a45oMFI.Uc70w.OIfg50G3j8ijLzpyw0Fq4UNJW', 'ROLE_CUSTOMER'),   -- customer123
('Ana Torres', 'ana@customer.com', '$2b$12$untYEXp7qhnWvQMUxBCKU.jB45pz3oEoSuXTHpsWtBf2xJcFQsPTy', 'ROLE_CUSTOMER');    -- customer456

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
INSERT INTO notifications (type, message, sent, user_id, created_at) VALUES
('EMAIL', 'Tu reserva #1 ha sido confirmada.', true, 3, NOW()),
('SMS', 'Recordatorio: reserva #2 pendiente de pago.', false, 4, NOW()),
('EMAIL', 'Tu reserva #3 fue cancelada.', true, 3, NOW());