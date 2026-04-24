package com.lavado.api.dto.response;

import com.lavado.api.enums.EstadoPago;
import com.lavado.api.enums.MetodoPago;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoResponse {
    private Long id;
    private Long ordenId;
    private BigDecimal monto;
    private MetodoPago metodo;
    private EstadoPago estado;
    private LocalDateTime pagadoEn;
    private String referencia;
}
