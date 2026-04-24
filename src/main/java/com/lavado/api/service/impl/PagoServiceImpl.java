package com.lavado.api.service.impl;

import com.lavado.api.dto.request.PagoRequest;
import com.lavado.api.dto.response.PagoResponse;
import com.lavado.api.entity.OrdenServicio;
import com.lavado.api.entity.Pago;
import com.lavado.api.enums.EstadoOrden;
import com.lavado.api.enums.EstadoPago;
import com.lavado.api.exception.BusinessException;
import com.lavado.api.exception.ResourceNotFoundException;
import com.lavado.api.repository.OrdenServicioRepository;
import com.lavado.api.repository.PagoRepository;
import com.lavado.api.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final OrdenServicioRepository ordenRepository;

    @Override
    public PagoResponse registrar(Long ordenId, PagoRequest req) {
        OrdenServicio orden = ordenRepository.findByIdWithDetails(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", ordenId));

        if (orden.getEstado() == EstadoOrden.CANCELADA)
            throw new BusinessException("No se puede registrar pago en una orden cancelada");
        if (orden.getEstado() == EstadoOrden.REGISTRADA)
            throw new BusinessException("La orden debe estar EN_PROCESO o FINALIZADA para recibir pagos");

        BigDecimal totalOrden = orden.calcularTotal();
        BigDecimal totalPagado = orden.getPagos().stream()
                .filter(p -> p.getEstado() == EstadoPago.COMPLETADO)
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saldoPendiente = totalOrden.subtract(totalPagado);

        if (req.getMonto().compareTo(saldoPendiente) > 0)
            throw new BusinessException("El monto $" + req.getMonto()
                    + " supera el saldo pendiente de $" + saldoPendiente);

        Pago pago = Pago.builder()
                .orden(orden)
                .monto(req.getMonto())
                .metodo(req.getMetodo())
                .referencia(req.getReferencia())
                .estado(EstadoPago.COMPLETADO)
                .build();

        return toResponse(pagoRepository.save(pago));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorOrden(Long ordenId) {
        ordenRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", ordenId));
        return pagoRepository.findByOrdenIdOrderByPagadoEnDesc(ordenId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public PagoResponse anular(Long id) {
        Pago pago = findOrThrow(id);
        if (pago.getEstado() == EstadoPago.ANULADO)
            throw new BusinessException("El pago ya esta anulado");
        pago.setEstado(EstadoPago.ANULADO);
        return toResponse(pagoRepository.save(pago));
    }

    private Pago findOrThrow(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", id));
    }

    private PagoResponse toResponse(Pago p) {
        PagoResponse r = new PagoResponse();
        r.setId(p.getId());
        r.setOrdenId(p.getOrden().getId());
        r.setMonto(p.getMonto());
        r.setMetodo(p.getMetodo());
        r.setEstado(p.getEstado());
        r.setPagadoEn(p.getPagadoEn());
        r.setReferencia(p.getReferencia());
        return r;
    }
}
