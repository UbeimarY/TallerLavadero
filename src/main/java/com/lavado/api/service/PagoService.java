package com.lavado.api.service;

import com.lavado.api.dto.request.PagoRequest;
import com.lavado.api.dto.response.PagoResponse;
import java.util.List;

public interface PagoService {
    PagoResponse registrar(Long ordenId, PagoRequest request);
    List<PagoResponse> listarPorOrden(Long ordenId);
    PagoResponse buscarPorId(Long id);
    PagoResponse anular(Long id);
}
