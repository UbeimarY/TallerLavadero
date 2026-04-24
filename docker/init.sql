-- Script de inicialización ejecutado una sola vez al crear el contenedor
-- Hibernate (ddl-auto=update) creará las tablas automáticamente;
-- este script sólo garantiza que la BD y el usuario existan.

-- Asegurar extensiones útiles
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";  -- para búsquedas de texto futuras

-- Comentario descriptivo en el esquema público
COMMENT ON SCHEMA public IS 'Sistema de gestión - Centro de Lavado de Vehículos';
