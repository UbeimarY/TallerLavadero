package com.lavado.api.service.impl;

import com.lavado.api.dto.request.VehiculoRequest;
import com.lavado.api.dto.response.VehiculoResponse;
import com.lavado.api.entity.Cliente;
import com.lavado.api.entity.Vehiculo;
import com.lavado.api.exception.BusinessException;
import com.lavado.api.exception.ResourceNotFoundException;
import com.lavado.api.repository.ClienteRepository;
import com.lavado.api.repository.VehiculoRepository;
import com.lavado.api.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public VehiculoResponse crear(VehiculoRequest req) {
        if (vehiculoRepository.existsByPlaca(req.getPlaca().toUpperCase()))
            throw new BusinessException("Ya existe un vehiculo con placa: " + req.getPlaca());

        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", req.getClienteId()));

        Vehiculo v = Vehiculo.builder()
                .placa(req.getPlaca().toUpperCase())
                .marca(req.getMarca())
                .modelo(req.getModelo())
                .anio(req.getAnio())
                .tipo(req.getTipo())
                .color(req.getColor())
                .cliente(cliente)
                .build();
        return toResponse(vehiculoRepository.save(v));
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponse> listar() {
        return vehiculoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponse> listarPorCliente(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId).stream().map(this::toResponse).toList();
    }

    @Override
    public VehiculoResponse actualizar(Long id, VehiculoRequest req) {
        Vehiculo v = findOrThrow(id);
        v.setMarca(req.getMarca());
        v.setModelo(req.getModelo());
        v.setAnio(req.getAnio());
        v.setTipo(req.getTipo());
        v.setColor(req.getColor());
        return toResponse(vehiculoRepository.save(v));
    }

    private Vehiculo findOrThrow(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo", id));
    }

    public VehiculoResponse toResponse(Vehiculo v) {
        VehiculoResponse r = new VehiculoResponse();
        r.setId(v.getId());
        r.setPlaca(v.getPlaca());
        r.setMarca(v.getMarca());
        r.setModelo(v.getModelo());
        r.setAnio(v.getAnio());
        r.setTipo(v.getTipo());
        r.setColor(v.getColor());
        r.setClienteId(v.getCliente().getId());
        r.setClienteNombre(v.getCliente().getNombre() + " " + v.getCliente().getApellido());
        return r;
    }
}
