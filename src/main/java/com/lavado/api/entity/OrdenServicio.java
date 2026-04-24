package com.lavado.api.entity;

import com.lavado.api.enums.EstadoOrden;
import com.lavado.api.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes_servicio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 25)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoOrden estado = EstadoOrden.REGISTRADA;

    @Column(length = 500)
    private String observaciones;

    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    @Column(name = "actualizada_en")
    private LocalDateTime actualizadaEn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DetalleOrden> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Pago> pagos = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        this.creadaEn = LocalDateTime.now();
        this.actualizadaEn = LocalDateTime.now();
        if (this.codigo == null) {
            this.codigo = "ORD-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(creadaEn);
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.actualizadaEn = LocalDateTime.now();
    }

    public BigDecimal calcularTotal() {
        return detalles.stream()
                .map(DetalleOrden::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cambiarEstado(EstadoOrden nuevoEstado) {
        boolean valida = switch (this.estado) {
            case REGISTRADA -> nuevoEstado == EstadoOrden.EN_PROCESO  || nuevoEstado == EstadoOrden.CANCELADA;
            case EN_PROCESO -> nuevoEstado == EstadoOrden.FINALIZADA  || nuevoEstado == EstadoOrden.CANCELADA;
            case FINALIZADA -> nuevoEstado == EstadoOrden.ENTREGADA;
            case ENTREGADA, CANCELADA -> false;
        };
        if (!valida) {
            throw new BusinessException("Transicion invalida de estado: " + this.estado + " -> " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }

    public boolean esCancelable() {
        return this.estado == EstadoOrden.REGISTRADA || this.estado == EstadoOrden.EN_PROCESO;
    }
}
