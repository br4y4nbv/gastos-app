package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.Gasto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class GastoSpecs {

    private GastoSpecs() {}

    // Construye el WHERE dinámicamente.
    // root = la tabla gastos, cb = fábrica de condiciones (CriteriaBuilder)
    public static Specification<Gasto> filtrar(Long usuarioId, LocalDate desde,
                                               LocalDate hasta, Long categoriaId) {
        return (root, query, cb) -> {
            List<Predicate> condiciones = new ArrayList<>();

            // Siempre: solo los gastos del usuario (seguridad)
            condiciones.add(cb.equal(root.get("usuario").get("id"), usuarioId));

            // Los demás solo se agregan si el usuario los envió
            if (desde != null) {
                condiciones.add(cb.greaterThanOrEqualTo(root.get("fecha"), desde));
            }
            if (hasta != null) {
                condiciones.add(cb.lessThanOrEqualTo(root.get("fecha"), hasta));
            }
            if (categoriaId != null) {
                condiciones.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }

            // Une todas las condiciones con AND
            return cb.and(condiciones.toArray(new Predicate[0]));
        };
    }
}