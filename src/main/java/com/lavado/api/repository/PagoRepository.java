package com.lavado.api.repository;

import com.lavado.api.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByOrdenIdOrderByPagadoEnDesc(Long ordenId);
}
