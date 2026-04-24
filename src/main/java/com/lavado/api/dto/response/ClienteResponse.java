package com.lavado.api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClienteResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private String email;
    private LocalDateTime creadoEn;
    private List<VehiculoResponse> vehiculos;
}
