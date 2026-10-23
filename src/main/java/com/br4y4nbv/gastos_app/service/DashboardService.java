package com.br4y4nbv.gastos_app.service;

import com.br4y4nbv.gastos_app.dto.ResumenMensual;
import com.br4y4nbv.gastos_app.model.Gasto;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final GastoRepository gastoRepository;
    private final UsuarioRepository usuarioRepository;

    private Long usuarioId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email).orElseThrow().getId();
    }

    @Transactional(readOnly = true)
    public ResumenMensual resumenDelMes() {
        Long uid = usuarioId();
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.withDayOfMonth(1);
        LocalDate fin = hoy.withDayOfMonth(hoy.lengthOfMonth());

        BigDecimal total = gastoRepository.sumarEntre(uid, inicio, fin);
        long cantidad = gastoRepository.contarEntre(uid, inicio, fin);

        // Evita dividir entre cero cuando no hay gastos
        BigDecimal promedio = cantidad == 0
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(cantidad), 2, RoundingMode.HALF_UP);

        List<String> etiquetas = new ArrayList<>();
        List<BigDecimal> valores = new ArrayList<>();
        for (Object[] fila : gastoRepository.totalesPorCategoria(uid, inicio, fin)) {
            etiquetas.add(fila[0] != null ? (String) fila[0] : "Sin categoría");
            valores.add((BigDecimal) fila[1]);
        }

        return new ResumenMensual(total, cantidad, promedio, etiquetas, valores);
    }

    @Transactional(readOnly = true)
    public List<Gasto> ultimosGastos() {
        return gastoRepository.findTop5ByUsuarioIdOrderByFechaDescIdDesc(usuarioId());
    }
}