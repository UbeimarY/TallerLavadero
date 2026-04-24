package com.lavado.api.service.impl;

import com.lavado.api.dto.request.ClienteRequest;
import com.lavado.api.dto.response.ClienteResponse;
import com.lavado.api.dto.response.VehiculoResponse;
import com.lavado.api.entity.Cliente;
import com.lavado.api.exception.BusinessException;
import com.lavado.api.exception.ResourceNotFoundException;
import com.lavado.api.repository.ClienteRepository;
import com.lavado.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse crear(ClienteRequest req) {
        if (clienteRepository.existsByDocumento(req.getDocumento()))
            throw new BusinessException("Ya existe un cliente con documento: " + req.getDocumento());
        if (req.getEmail() != null && !req.getEmail().isBlank() && clienteRepository.existsByEmail(req.getEmail()))
            throw new BusinessException("Ya existe un cliente con email: " + req.getEmail());

        Cliente cliente = Cliente.builder()
                .nombre(req.getNombre())
                .apellido(req.getApellido())
                .documento(req.getDocumento())
                .telefono(req.getTelefono())
                .email(req.getEmail())
                .build();
        return toResponse(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ClienteResponse actualizar(Long id, ClienteRequest req) {
        Cliente c = findOrThrow(id);
        c.setNombre(req.getNombre());
        c.setApellido(req.getApellido());
        c.setTelefono(req.getTelefono());
        c.setEmail(req.getEmail());
        return toResponse(clienteRepository.save(c));
    }

    @Override
    public void eliminar(Long id) {
        findOrThrow(id);
        clienteRepository.deleteById(id);
    }

    private Cliente findOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private ClienteResponse toResponse(Cliente c) {
        ClienteResponse r = new ClienteResponse();
        r.setId(c.getId());
        r.setNombre(c.getNombre());
        r.setApellido(c.getApellido());
        r.setDocumento(c.getDocumento());
        r.setTelefono(c.getTelefono());
        r.setEmail(c.getEmail());
        r.setCreadoEn(c.getCreadoEn());
        if (c.getVehiculos() != null) {
            r.setVehiculos(c.getVehiculos().stream().map(v -> {
                VehiculoResponse vr = new VehiculoResponse();
                vr.setId(v.getId());
                vr.setPlaca(v.getPlaca());
                vr.setMarca(v.getMarca());
                vr.setModelo(v.getModelo());
                vr.setAnio(v.getAnio());
                vr.setTipo(v.getTipo());
                vr.setColor(v.getColor());
                return vr;
            }).toList());
        }
        return r;
    }
}
