package com.lavado.api.dto.response;

import com.lavado.api.enums.EstadoOrden;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdenResponse {
    private Long id;
    private String codigo;
    private EstadoOrden estado;
    private String observaciones;
    private LocalDateTime creadaEn;
    private LocalDateTime actualizadaEn;
    private Long clienteId;
    private String clienteNombre;
    private Long vehiculoId;
    private String vehiculoPlaca;
    private String vehiculoDescripcion;
    private BigDecimal total;
    private List<DetalleResponse> detalles;
    private List<PagoResponse> pagos;
}
