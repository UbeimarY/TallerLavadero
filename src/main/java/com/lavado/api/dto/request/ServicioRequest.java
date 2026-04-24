package com.lavado.api.dto.request;

import com.lavado.api.enums.TipoServicio;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioRequest {
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Size(max = 300)
    private String descripcion;

    @NotNull(message = "El tipo de servicio es obligatorio")
    private TipoServicio tipo;

    @DecimalMin(value = "0.0", message = "El precio base no puede ser negativo")
    private BigDecimal precioBase;
}
