package com.lavado.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class OrdenRequest {
    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @Size(max = 500)
    private String observaciones;

    @NotEmpty(message = "La orden debe tener al menos un servicio")
    @Valid
    private List<DetalleRequest> detalles;
}
