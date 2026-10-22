package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.Gasto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

// JpaSpecificationExecutor agrega findAll(Specification, Pageable)
public interface GastoRepository extends JpaRepository<Gasto, Long>, JpaSpecificationExecutor<Gasto>  {

    //EntityGraph carga la categoria en la misma consulta y evita el problema de las N+1 consultas
    @EntityGraph(attributePaths = "categoria")
    Page<Gasto> findByUsuarioId(Long usuarioId, Pageable pageable);

    //findByIdAndUsuarioId garantiza que nadie edite gastos ajenos
    Optional<Gasto> findByIdAndUsuarioId(Long id, Long usuarioId);

    // Se sobrescribe solo para agregarle @EntityGraph y que la categoría
    // venga en la misma consulta (evita el problema N+1 también con filtros)
    @Override
    @EntityGraph(attributePaths = "categoria")
    Page<Gasto> findAll(Specification<Gasto> spec, Pageable pageable);
}