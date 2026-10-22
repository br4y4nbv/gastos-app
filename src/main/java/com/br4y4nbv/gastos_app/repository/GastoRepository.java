package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.Gasto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    //EntityGraph carga la categoria en la misma consulta y evita el problema de las N+1 consultas
    @EntityGraph(attributePaths = "categoria")
    Page<Gasto> findByUsuarioId(Long usuarioId, Pageable pageable);

    //findByIdAndUsuarioId garantiza que nadie edite gastos ajenos
    Optional<Gasto> findByIdAndUsuarioId(Long id, Long usuarioId);
}