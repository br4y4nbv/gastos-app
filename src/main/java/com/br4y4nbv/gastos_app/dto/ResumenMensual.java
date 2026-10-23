package com.br4y4nbv.gastos_app.dto;

import java.math.BigDecimal;
import java.util.List;

public record ResumenMensual(
        BigDecimal total,
        long cantidad,
        BigDecimal promedio,
        List<String> etiquetas,       // nombres de categorías, para el gráfico
        List<BigDecimal> valores) {   // sumas, en el mismo orden
}