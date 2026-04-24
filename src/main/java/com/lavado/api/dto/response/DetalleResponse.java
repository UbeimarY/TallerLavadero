package com.lavado.api.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleResponse {
    private Long id;
    private Long servicioId;
    private String servicioNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String nota;
}
