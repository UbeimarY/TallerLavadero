package com.lavado.api.controller;

import com.lavado.api.dto.request.PagoRequest;
import com.lavado.api.dto.response.PagoResponse;
import com.lavado.api.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Registro y gestion de pagos por orden")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping("/ordenes/{ordenId}/pagos")
    @Operation(summary = "Registrar pago para una orden")
    public ResponseEntity<PagoResponse> registrar(
            @PathVariable Long ordenId,
            @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.registrar(ordenId, request));
    }

    @GetMapping("/ordenes/{ordenId}/pagos")
    @Operation(summary = "Listar pagos de una orden")
    public ResponseEntity<List<PagoResponse>> listarPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.listarPorOrden(ordenId));
    }

    @GetMapping("/pagos/{id}")
    @Operation(summary = "Obtener pago por ID")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PatchMapping("/pagos/{id}/anular")
    @Operation(summary = "Anular un pago")
    public ResponseEntity<PagoResponse> anular(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.anular(id));
    }
}
