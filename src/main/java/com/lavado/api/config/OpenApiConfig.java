package com.lavado.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Centro de Lavado de Vehiculos")
                        .description("Sistema de gestion: clientes, vehiculos, ordenes de servicio y pagos. " +
                                     "BD: lavadero_db @ PostgreSQL 5432")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@lavado.com")));
    }
}
