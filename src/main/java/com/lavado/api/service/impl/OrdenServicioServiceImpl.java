package com.lavado.api.service.impl;

import com.lavado.api.dto.request.DetalleRequest;
import com.lavado.api.dto.request.OrdenRequest;
import com.lavado.api.dto.response.*;
import com.lavado.api.entity.*;
import com.lavado.api.enums.EstadoOrden;
import com.lavado.api.exception.BusinessException;
import com.lavado.api.exception.ResourceNotFoundException;
import com.lavado.api.repository.*;
import com.lavado.api.service.OrdenServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository ordenRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;

    @Override
    public OrdenResponse crear(OrdenRequest req) {
        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", req.getClienteId()));

        Vehiculo vehiculo = vehiculoRepository.findById(req.getVehiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo", req.getVehiculoId()));

        if (!vehiculo.getCliente().getId().equals(cliente.getId()))
            throw new BusinessException("El vehiculo no pertenece al cliente indicado");

        if (req.getDetalles() == null || req.getDetalles().isEmpty())
            throw new BusinessException("La orden debe tener al menos un servicio");

        OrdenServicio orden = OrdenServicio.builder()
                .cliente(cliente)
                .vehiculo(vehiculo)
                .observaciones(req.getObservaciones())
                .estado(EstadoOrden.REGISTRADA)
                .build();

        List<DetalleOrden> detalles = new ArrayList<>();
        for (DetalleRequest dr : req.getDetalles()) {
            Servicio servicio = servicioRepository.findById(dr.getServicioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Servicio", dr.getServicioId()));
            if (!servicio.isActivo())
                throw new BusinessException("El servicio '" + servicio.getNombre() + "' no esta disponible");

            DetalleOrden detalle = DetalleOrden.builder()
                    .orden(orden)
                    .servicio(servicio)
                    .cantidad(dr.getCantidad())
                    .precioUnitario(servicio.calcularPrecio(vehiculo.getTipo()))
                    .nota(dr.getNota())
                    .build();
            detalles.add(detalle);
        }
        orden.setDetalles(detalles);
        return toResponse(ordenRepository.save(orden));
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenResponse buscarDetalle(Long id) {
        OrdenServicio orden = ordenRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", id));
        return toResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenResponse> listar() {
        return ordenRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenResponse> listarPorCliente(Long clienteId) {
        return ordenRepository.findByClienteIdOrderByCreadaEnDesc(clienteId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenResponse> listarPorEstado(EstadoOrden estado) {
        return ordenRepository.findByEstadoOrderByCreadaEnDesc(estado)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public OrdenResponse cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        OrdenServicio orden = findOrThrow(id);
        orden.cambiarEstado(nuevoEstado);
        return toResponse(ordenRepository.save(orden));
    }

    private OrdenServicio findOrThrow(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden", id));
    }

    public OrdenResponse toResponse(OrdenServicio o) {
        OrdenResponse r = new OrdenResponse();
        r.setId(o.getId());
        r.setCodigo(o.getCodigo());
        r.setEstado(o.getEstado());
        r.setObservaciones(o.getObservaciones());
        r.setCreadaEn(o.getCreadaEn());
        r.setActualizadaEn(o.getActualizadaEn());
        r.setClienteId(o.getCliente().getId());
        r.setClienteNombre(o.getCliente().getNombre() + " " + o.getCliente().getApellido());
        r.setVehiculoId(o.getVehiculo().getId());
        r.setVehiculoPlaca(o.getVehiculo().getPlaca());
        r.setVehiculoDescripcion(o.getVehiculo().getMarca() + " " + o.getVehiculo().getModelo()
                + " (" + o.getVehiculo().getAnio() + ")");
        r.setTotal(o.calcularTotal());

        if (o.getDetalles() != null) {
            r.setDetalles(o.getDetalles().stream().map(d -> {
                DetalleResponse dr = new DetalleResponse();
                dr.setId(d.getId());
                dr.setServicioId(d.getServicio().getId());
                dr.setServicioNombre(d.getServicio().getNombre());
                dr.setCantidad(d.getCantidad());
                dr.setPrecioUnitario(d.getPrecioUnitario());
                dr.setSubtotal(d.getSubtotal());
                dr.setNota(d.getNota());
                return dr;
            }).toList());
        }

        if (o.getPagos() != null) {
            r.setPagos(o.getPagos().stream().map(p -> {
                PagoResponse pr = new PagoResponse();
                pr.setId(p.getId());
                pr.setOrdenId(o.getId());
                pr.setMonto(p.getMonto());
                pr.setMetodo(p.getMetodo());
                pr.setEstado(p.getEstado());
                pr.setPagadoEn(p.getPagadoEn());
                pr.setReferencia(p.getReferencia());
                return pr;
            }).toList());
        }
        return r;
    }
}
