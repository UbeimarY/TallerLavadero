package com.lavado.api.service;

import com.lavado.api.dto.request.OrdenRequest;
import com.lavado.api.dto.response.OrdenResponse;
import com.lavado.api.enums.EstadoOrden;
import java.util.List;

public interface OrdenServicioService {
    OrdenResponse crear(OrdenRequest request);
    OrdenResponse buscarPorId(Long id);
    OrdenResponse buscarDetalle(Long id);
    List<OrdenResponse> listar();
    List<OrdenResponse> listarPorCliente(Long clienteId);
    List<OrdenResponse> listarPorEstado(EstadoOrden estado);
    OrdenResponse cambiarEstado(Long id, EstadoOrden nuevoEstado);
}
