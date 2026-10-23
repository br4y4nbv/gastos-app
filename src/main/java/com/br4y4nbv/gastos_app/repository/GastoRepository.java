package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.Gasto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Suma de un rango de fechas. COALESCE devuelve 0 cuando no hay gastos
    // (sin él, SUM devuelve null y habría que tratarlo aparte).
    @Query("""
            SELECT COALESCE(SUM(g.monto), 0)
            FROM Gasto g
            WHERE g.usuario.id = :usuarioId
            AND g.fecha >= :desde AND g.fecha <= :hasta
            """)
    BigDecimal sumarEntre(@Param("usuarioId") Long usuarioId,
                        @Param("desde") LocalDate desde,
                        @Param("hasta") LocalDate hasta);

    @Query("""
            SELECT COUNT(g)
            FROM Gasto g
            WHERE g.usuario.id = :usuarioId
            AND g.fecha >= :desde AND g.fecha <= :hasta
            """)
    long contarEntre(@Param("usuarioId") Long usuarioId,
                    @Param("desde") LocalDate desde,
                    @Param("hasta") LocalDate hasta);

    // Total por categoría. Devuelve filas [nombreCategoria, suma] ordenadas de mayor a menor.
    // El LEFT JOIN conserva los gastos sin categoría (aparecerían con nombre null).
    @Query("""
            SELECT c.nombre, SUM(g.monto)
            FROM Gasto g LEFT JOIN g.categoria c
            WHERE g.usuario.id = :usuarioId
            AND g.fecha >= :desde AND g.fecha <= :hasta
            GROUP BY c.nombre
            ORDER BY SUM(g.monto) DESC
            """)
    List<Object[]> totalesPorCategoria(@Param("usuarioId") Long usuarioId,
                                    @Param("desde") LocalDate desde,
                                    @Param("hasta") LocalDate hasta);

    // Los 5 más recientes, con la categoría cargada en la misma consulta
    @EntityGraph(attributePaths = "categoria")
    List<Gasto> findTop5ByUsuarioIdOrderByFechaDescIdDesc(Long usuarioId);
}