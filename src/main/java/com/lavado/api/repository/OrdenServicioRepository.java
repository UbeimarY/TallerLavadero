package com.lavado.api.repository;

import com.lavado.api.entity.OrdenServicio;
import com.lavado.api.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {
    List<OrdenServicio> findByClienteIdOrderByCreadaEnDesc(Long clienteId);
    List<OrdenServicio> findByVehiculoId(Long vehiculoId);
    List<OrdenServicio> findByEstadoOrderByCreadaEnDesc(EstadoOrden estado);
    Optional<OrdenServicio> findByCodigo(String codigo);

    @Query("SELECT o FROM OrdenServicio o " +
           "LEFT JOIN FETCH o.detalles d LEFT JOIN FETCH d.servicio " +
           "LEFT JOIN FETCH o.pagos " +
           "WHERE o.id = :id")
    Optional<OrdenServicio> findByIdWithDetails(@Param("id") Long id);
}
