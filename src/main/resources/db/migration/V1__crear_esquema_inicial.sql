-- ============================================================
-- V1 - Esquema inicial: Centro de Lavado de Vehiculos
-- Base de datos: lavadero_db | Usuario: postgres
-- ============================================================

-- Tipos ENUM de PostgreSQL
CREATE TYPE tipo_vehiculo_enum AS ENUM (
    'MOTO', 'AUTO', 'CAMIONETA', 'CAMPERO', 'BUS', 'CAMION'
);

CREATE TYPE tipo_servicio_enum AS ENUM (
    'LAVADO_BASICO', 'LAVADO_PREMIUM', 'POLICHADO',
    'DESINFECCION_INTERNA', 'CAMBIO_ACEITE', 'OTRO'
);

CREATE TYPE estado_orden_enum AS ENUM (
    'REGISTRADA', 'EN_PROCESO', 'FINALIZADA', 'ENTREGADA', 'CANCELADA'
);

CREATE TYPE metodo_pago_enum AS ENUM (
    'EFECTIVO', 'TARJETA_CREDITO', 'TARJETA_DEBITO', 'TRANSFERENCIA', 'OTRO'
);

CREATE TYPE estado_pago_enum AS ENUM (
    'PENDIENTE', 'COMPLETADO', 'ANULADO'
);

-- ── CLIENTES ──────────────────────────────────────────────────────────────
CREATE TABLE clientes (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    apellido    VARCHAR(100)    NOT NULL,
    documento   VARCHAR(20)     NOT NULL,
    telefono    VARCHAR(20)     NOT NULL,
    email       VARCHAR(150),
    creado_en   TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_clientes_documento UNIQUE (documento),
    CONSTRAINT uq_clientes_email    UNIQUE (email)
);

-- ── VEHICULOS ─────────────────────────────────────────────────────────────
CREATE TABLE vehiculos (
    id          BIGSERIAL           PRIMARY KEY,
    placa       VARCHAR(10)         NOT NULL,
    marca       VARCHAR(50)         NOT NULL,
    modelo      VARCHAR(50)         NOT NULL,
    anio        INTEGER             NOT NULL,
    tipo        VARCHAR(20)         NOT NULL,
    color       VARCHAR(30),
    cliente_id  BIGINT              NOT NULL,
    CONSTRAINT uq_vehiculos_placa   UNIQUE (placa),
    CONSTRAINT chk_vehiculos_anio   CHECK (anio BETWEEN 1900 AND 2100),
    CONSTRAINT fk_vehiculos_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes(id) ON DELETE RESTRICT
);

-- ── SERVICIOS ─────────────────────────────────────────────────────────────
CREATE TABLE servicios (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL,
    descripcion VARCHAR(300),
    tipo        VARCHAR(30)     NOT NULL,
    activo      BOOLEAN         NOT NULL DEFAULT TRUE,
    precio_base NUMERIC(12,2),
    CONSTRAINT chk_servicios_precio CHECK (precio_base IS NULL OR precio_base >= 0)
);

-- ── PRECIOS POR TIPO DE VEHICULO ─────────────────────────────────────────
CREATE TABLE precios_servicio (
    id              BIGSERIAL       PRIMARY KEY,
    servicio_id     BIGINT          NOT NULL,
    tipo_vehiculo   VARCHAR(20)     NOT NULL,
    precio          NUMERIC(12,2)   NOT NULL,
    CONSTRAINT uq_precios_servicio_tipo UNIQUE (servicio_id, tipo_vehiculo),
    CONSTRAINT chk_precios_precio       CHECK (precio >= 0),
    CONSTRAINT fk_precios_servicio      FOREIGN KEY (servicio_id)
        REFERENCES servicios(id) ON DELETE CASCADE
);

-- ── ORDENES DE SERVICIO ───────────────────────────────────────────────────
CREATE TABLE ordenes_servicio (
    id              BIGSERIAL       PRIMARY KEY,
    codigo          VARCHAR(25)     NOT NULL,
    estado          VARCHAR(20)     NOT NULL DEFAULT 'REGISTRADA',
    observaciones   VARCHAR(500),
    cliente_id      BIGINT          NOT NULL,
    vehiculo_id     BIGINT          NOT NULL,
    creada_en       TIMESTAMP       NOT NULL DEFAULT NOW(),
    actualizada_en  TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_ordenes_codigo       UNIQUE (codigo),
    CONSTRAINT fk_ordenes_cliente      FOREIGN KEY (cliente_id)
        REFERENCES clientes(id) ON DELETE RESTRICT,
    CONSTRAINT fk_ordenes_vehiculo     FOREIGN KEY (vehiculo_id)
        REFERENCES vehiculos(id) ON DELETE RESTRICT
);

-- ── DETALLES DE ORDEN ─────────────────────────────────────────────────────
CREATE TABLE detalles_orden (
    id              BIGSERIAL       PRIMARY KEY,
    orden_id        BIGINT          NOT NULL,
    servicio_id     BIGINT          NOT NULL,
    cantidad        INTEGER         NOT NULL DEFAULT 1,
    precio_unitario NUMERIC(12,2)   NOT NULL,
    nota            VARCHAR(200),
    CONSTRAINT chk_detalles_cantidad  CHECK (cantidad > 0),
    CONSTRAINT chk_detalles_precio    CHECK (precio_unitario >= 0),
    CONSTRAINT fk_detalles_orden      FOREIGN KEY (orden_id)
        REFERENCES ordenes_servicio(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalles_servicio   FOREIGN KEY (servicio_id)
        REFERENCES servicios(id) ON DELETE RESTRICT
);

-- ── PAGOS ─────────────────────────────────────────────────────────────────
CREATE TABLE pagos (
    id          BIGSERIAL       PRIMARY KEY,
    orden_id    BIGINT          NOT NULL,
    monto       NUMERIC(12,2)   NOT NULL,
    metodo      VARCHAR(20)     NOT NULL,
    estado      VARCHAR(15)     NOT NULL DEFAULT 'COMPLETADO',
    pagado_en   TIMESTAMP       NOT NULL DEFAULT NOW(),
    referencia  VARCHAR(100),
    CONSTRAINT chk_pagos_monto  CHECK (monto > 0),
    CONSTRAINT fk_pagos_orden   FOREIGN KEY (orden_id)
        REFERENCES ordenes_servicio(id) ON DELETE RESTRICT
);

-- ── INDICES ───────────────────────────────────────────────────────────────
CREATE INDEX idx_vehiculos_cliente     ON vehiculos(cliente_id);
CREATE INDEX idx_ordenes_cliente       ON ordenes_servicio(cliente_id);
CREATE INDEX idx_ordenes_vehiculo      ON ordenes_servicio(vehiculo_id);
CREATE INDEX idx_ordenes_estado        ON ordenes_servicio(estado);
CREATE INDEX idx_ordenes_creada_en     ON ordenes_servicio(creada_en DESC);
CREATE INDEX idx_detalles_orden        ON detalles_orden(orden_id);
CREATE INDEX idx_detalles_servicio     ON detalles_orden(servicio_id);
CREATE INDEX idx_pagos_orden           ON pagos(orden_id);
CREATE INDEX idx_precios_servicio      ON precios_servicio(servicio_id);
