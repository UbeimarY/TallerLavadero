package com.lavado.api.entity;

import com.lavado.api.enums.EstadoPago;
import com.lavado.api.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenServicio orden;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private EstadoPago estado = EstadoPago.COMPLETADO;

    @Column(name = "pagado_en", nullable = false)
    private LocalDateTime pagadoEn;

    @Column(length = 100)
    private String referencia;

    @PrePersist
    private void prePersist() {
        if (this.pagadoEn == null) this.pagadoEn = LocalDateTime.now();
    }

    public boolean esValido() {
        return this.monto != null
                && this.monto.compareTo(BigDecimal.ZERO) > 0
                && this.estado != EstadoPago.ANULADO;
    }
}
