package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.Gasto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    Page<Gasto> findByUsuarioId(Long usuarioId, Pageable pageable);
}
