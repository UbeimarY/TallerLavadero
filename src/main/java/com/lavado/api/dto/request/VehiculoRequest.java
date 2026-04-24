package com.lavado.api.dto.request;

import com.lavado.api.enums.TipoVehiculo;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehiculoRequest {
    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 10)
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50)
    private String modelo;

    @NotNull(message = "El anio es obligatorio")
    @Min(value = 1900, message = "El anio minimo es 1900")
    @Max(value = 2100, message = "El anio maximo es 2100")
    private Integer anio;

    @NotNull(message = "El tipo de vehiculo es obligatorio")
    private TipoVehiculo tipo;

    @Size(max = 30)
    private String color;

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;
}
