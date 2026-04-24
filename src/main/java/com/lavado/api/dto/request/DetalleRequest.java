package com.lavado.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DetalleRequest {
    @NotNull(message = "El servicioId es obligatorio")
    private Long servicioId;

    @NotNull @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;

    @Size(max = 200)
    private String nota;
}
