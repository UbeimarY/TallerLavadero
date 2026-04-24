package com.lavado.api.dto.response;

import com.lavado.api.enums.TipoVehiculo;
import lombok.Data;

@Data
public class VehiculoResponse {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private TipoVehiculo tipo;
    private String color;
    private Long clienteId;
    private String clienteNombre;
}
