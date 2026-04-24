-- ============================================================
-- V2 - Datos iniciales del catalogo de servicios
-- Se ejecuta una sola vez al iniciar la aplicacion
-- ============================================================

-- ── SERVICIOS ─────────────────────────────────────────────────────────────
INSERT INTO servicios (nombre, descripcion, tipo, activo, precio_base) VALUES
('Lavado Basico',        'Lavado exterior con agua a presion y jabon',             'LAVADO_BASICO',       TRUE, NULL),
('Lavado Premium',       'Lavado exterior + aspirado + aromatizante interior',      'LAVADO_PREMIUM',      TRUE, NULL),
('Polichado',            'Pulido y polichado de carroceria con cera profesional',   'POLICHADO',           TRUE, 120000.00),
('Desinfeccion Interna', 'Tratamiento con ozono y desinfectante para habitaculo',   'DESINFECCION_INTERNA',TRUE,  50000.00),
('Cambio de Aceite',     'Cambio de aceite de motor (mano de obra sin repuesto)',   'CAMBIO_ACEITE',       TRUE,  30000.00);

-- ── PRECIOS LAVADO BASICO (id=1) ──────────────────────────────────────────
INSERT INTO precios_servicio (servicio_id, tipo_vehiculo, precio) VALUES
(1, 'MOTO',       15000.00),
(1, 'AUTO',       25000.00),
(1, 'CAMIONETA',  35000.00),
(1, 'CAMPERO',    40000.00),
(1, 'BUS',        80000.00),
(1, 'CAMION',    100000.00);

-- ── PRECIOS LAVADO PREMIUM (id=2) ─────────────────────────────────────────
INSERT INTO precios_servicio (servicio_id, tipo_vehiculo, precio) VALUES
(2, 'MOTO',       25000.00),
(2, 'AUTO',       45000.00),
(2, 'CAMIONETA',  60000.00),
(2, 'CAMPERO',    70000.00),
(2, 'BUS',       140000.00),
(2, 'CAMION',    180000.00);

-- ── CLIENTES DE DEMO ──────────────────────────────────────────────────────
INSERT INTO clientes (nombre, apellido, documento, telefono, email, creado_en) VALUES
('Carlos',  'Rodriguez', '1010101010', '3001234567', 'carlos@email.com',  NOW()),
('Maria',   'Lopez',     '2020202020', '3107654321', 'maria@email.com',   NOW()),
('Andres',  'Gomez',     '3030303030', '3209876543', 'andres@email.com',  NOW());

-- ── VEHICULOS DE DEMO ─────────────────────────────────────────────────────
INSERT INTO vehiculos (placa, marca, modelo, anio, tipo, color, cliente_id) VALUES
('ABC123', 'Toyota',    'Corolla', 2020, 'AUTO',     'Blanco', 1),
('XYZ789', 'Chevrolet', 'Tracker', 2022, 'CAMIONETA','Gris',   2),
('MOT001', 'Yamaha',    'FZ',      2021, 'MOTO',     'Negro',  1),
('CAM456', 'Mazda',     'CX-5',    2023, 'CAMPERO',  'Rojo',   3);

-- ── ORDEN DE DEMO (estado ENTREGADA con pago completado) ──────────────────
INSERT INTO ordenes_servicio (codigo, estado, observaciones, cliente_id, vehiculo_id, creada_en, actualizada_en) VALUES
('ORD-20240101000001', 'ENTREGADA', 'Orden de ejemplo ya finalizada', 1, 1, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day');

INSERT INTO detalles_orden (orden_id, servicio_id, cantidad, precio_unitario, nota) VALUES
(1, 1, 1, 25000.00, 'Lavado exterior completo'),
(1, 4, 1, 50000.00, 'Desinfeccion solicitada por el cliente');

INSERT INTO pagos (orden_id, monto, metodo, estado, pagado_en, referencia) VALUES
(1, 75000.00, 'EFECTIVO', 'COMPLETADO', NOW() - INTERVAL '1 day', NULL);

-- ── ORDEN DE DEMO (estado EN_PROCESO sin pago aun) ────────────────────────
INSERT INTO ordenes_servicio (codigo, estado, observaciones, cliente_id, vehiculo_id, creada_en, actualizada_en) VALUES
('ORD-20240101000002', 'EN_PROCESO', 'Solicita servicio rapido', 2, 2, NOW(), NOW());

INSERT INTO detalles_orden (orden_id, servicio_id, cantidad, precio_unitario, nota) VALUES
(2, 2, 1, 60000.00, 'Lavado premium camioneta');
