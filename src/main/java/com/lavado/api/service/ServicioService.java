package com.lavado.api.service;

import com.lavado.api.dto.request.ServicioRequest;
import com.lavado.api.dto.response.ServicioResponse;
import java.util.List;

public interface ServicioService {
    ServicioResponse crear(ServicioRequest request);
    ServicioResponse buscarPorId(Long id);
    List<ServicioResponse> listar();
    List<ServicioResponse> listarActivos();
    ServicioResponse actualizar(Long id, ServicioRequest request);
    void desactivar(Long id);
}
