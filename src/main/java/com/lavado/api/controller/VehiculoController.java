package com.lavado.api.controller;

import com.lavado.api.dto.request.VehiculoRequest;
import com.lavado.api.dto.response.VehiculoResponse;
import com.lavado.api.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehiculos", description = "Gestion de vehiculos registrados")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    @Operation(summary = "Registrar nuevo vehiculo")
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehiculos")
    public ResponseEntity<List<VehiculoResponse>> listar() {
        return ResponseEntity.ok(vehiculoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener vehiculo por ID")
    public ResponseEntity<VehiculoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar vehiculos de un cliente")
    public ResponseEntity<List<VehiculoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vehiculoService.listarPorCliente(clienteId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehiculo")
    public ResponseEntity<VehiculoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequest request) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, request));
    }
}
