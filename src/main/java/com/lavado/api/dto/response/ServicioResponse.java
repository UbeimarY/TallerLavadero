package com.lavado.api.dto.response;

import com.lavado.api.enums.TipoServicio;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServicioResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private TipoServicio tipo;
    private boolean activo;
    private BigDecimal precioBase;
}
