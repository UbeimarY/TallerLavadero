package com.lavado.api.entity;

import com.lavado.api.enums.TipoServicio;
import com.lavado.api.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoServicio tipo;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "precio_base", precision = 12, scale = 2)
    private BigDecimal precioBase;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<PrecioServicio> precios = new ArrayList<>();

    public BigDecimal calcularPrecio(TipoVehiculo tipoVehiculo) {
        return precios.stream()
                .filter(p -> p.getTipoVehiculo() == tipoVehiculo)
                .map(PrecioServicio::getPrecio)
                .findFirst()
                .orElse(precioBase != null ? precioBase : BigDecimal.ZERO);
    }
}
