package com.lavado.api.repository;

import com.lavado.api.entity.PrecioServicio;
import com.lavado.api.enums.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrecioServicioRepository extends JpaRepository<PrecioServicio, Long> {
    Optional<PrecioServicio> findByServicioIdAndTipoVehiculo(Long servicioId, TipoVehiculo tipoVehiculo);
}
