package com.lavado.api.config;

/**
 * Los datos iniciales son gestionados exclusivamente por Flyway.
 * Ver: src/main/resources/db/migration/V2__datos_iniciales.sql
 *
 * Esta clase se conserva vacia para mantener la estructura del paquete config
 * y facilitar la incorporacion futura de logica de inicializacion condicional.
 */
public class DataInitializer {
    // Flyway maneja todo: V1 crea el esquema, V2 inserta datos de catalogo y demo.
}
