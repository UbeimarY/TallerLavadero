package com.lavado.api.service.impl;

import com.lavado.api.dto.request.ServicioRequest;
import com.lavado.api.dto.response.ServicioResponse;
import com.lavado.api.entity.Servicio;
import com.lavado.api.exception.ResourceNotFoundException;
import com.lavado.api.repository.ServicioRepository;
import com.lavado.api.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Override
    public ServicioResponse crear(ServicioRequest req) {
        Servicio s = Servicio.builder()
                .nombre(req.getNombre())
                .descripcion(req.getDescripcion())
                .tipo(req.getTipo())
                .precioBase(req.getPrecioBase())
                .activo(true)
                .build();
        return toResponse(servicioRepository.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listar() {
        return servicioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponse> listarActivos() {
        return servicioRepository.findByActivoTrue().stream().map(this::toResponse).toList();
    }

    @Override
    public ServicioResponse actualizar(Long id, ServicioRequest req) {
        Servicio s = findOrThrow(id);
        s.setNombre(req.getNombre());
        s.setDescripcion(req.getDescripcion());
        s.setTipo(req.getTipo());
        s.setPrecioBase(req.getPrecioBase());
        return toResponse(servicioRepository.save(s));
    }

    @Override
    public void desactivar(Long id) {
        Servicio s = findOrThrow(id);
        s.setActivo(false);
        servicioRepository.save(s);
    }

    private Servicio findOrThrow(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", id));
    }

    private ServicioResponse toResponse(Servicio s) {
        ServicioResponse r = new ServicioResponse();
        r.setId(s.getId());
        r.setNombre(s.getNombre());
        r.setDescripcion(s.getDescripcion());
        r.setTipo(s.getTipo());
        r.setActivo(s.isActivo());
        r.setPrecioBase(s.getPrecioBase());
        return r;
    }
}
