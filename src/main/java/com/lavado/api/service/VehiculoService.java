package com.lavado.api.service;

import com.lavado.api.dto.request.VehiculoRequest;
import com.lavado.api.dto.response.VehiculoResponse;
import java.util.List;

public interface VehiculoService {
    VehiculoResponse crear(VehiculoRequest request);
    VehiculoResponse buscarPorId(Long id);
    List<VehiculoResponse> listar();
    List<VehiculoResponse> listarPorCliente(Long clienteId);
    VehiculoResponse actualizar(Long id, VehiculoRequest request);
}
