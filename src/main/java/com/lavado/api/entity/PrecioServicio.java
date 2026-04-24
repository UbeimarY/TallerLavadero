package com.lavado.api.entity;

import com.lavado.api.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "precios_servicio",
       uniqueConstraints = @UniqueConstraint(columnNames = {"servicio_id", "tipo_vehiculo"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrecioServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vehiculo", nullable = false, length = 20)
    private TipoVehiculo tipoVehiculo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
}
