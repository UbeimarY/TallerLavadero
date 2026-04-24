package com.lavado.api.controller;

import com.lavado.api.dto.request.OrdenRequest;
import com.lavado.api.dto.response.OrdenResponse;
import com.lavado.api.enums.EstadoOrden;
import com.lavado.api.service.OrdenServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
@Tag(name = "Ordenes de Servicio", description = "Gestion del ciclo de vida de las ordenes")
public class OrdenServicioController {

    private final OrdenServicioService ordenService;

    @PostMapping
    @Operation(summary = "Crear nueva orden de servicio")
    public ResponseEntity<OrdenResponse> crear(@Valid @RequestBody OrdenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar ordenes (opcionalmente filtrar por estado)")
    public ResponseEntity<List<OrdenResponse>> listar(
            @RequestParam(required = false) EstadoOrden estado) {
        if (estado != null) return ResponseEntity.ok(ordenService.listarPorEstado(estado));
        return ResponseEntity.ok(ordenService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID")
    public ResponseEntity<OrdenResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarPorId(id));
    }

    @GetMapping("/{id}/detalle")
    @Operation(summary = "Obtener detalle completo de una orden (servicios + pagos)")
    public ResponseEntity<OrdenResponse> buscarDetalle(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.buscarDetalle(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar ordenes de un cliente")
    public ResponseEntity<List<OrdenResponse>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ordenService.listarPorCliente(clienteId));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de una orden (maquina de estados)")
    public ResponseEntity<OrdenResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoOrden nuevoEstado) {
        return ResponseEntity.ok(ordenService.cambiarEstado(id, nuevoEstado));
    }
}
