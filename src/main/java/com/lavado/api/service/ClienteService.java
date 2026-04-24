package com.lavado.api.service;

import com.lavado.api.dto.request.ClienteRequest;
import com.lavado.api.dto.response.ClienteResponse;
import java.util.List;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);
    ClienteResponse buscarPorId(Long id);
    List<ClienteResponse> listar();
    ClienteResponse actualizar(Long id, ClienteRequest request);
    void eliminar(Long id);
}
