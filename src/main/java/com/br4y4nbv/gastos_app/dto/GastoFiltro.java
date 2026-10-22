package com.br4y4nbv.gastos_app.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Representa los parámetros de búsqueda que llegan por la URL:
// gastos?desde=2026-10-01&hasta=2026-10-31&categoriaId=2
// Todos son opcionales si vienen vacíos, valen null.
@Getter @Setter
public class GastoFiltro {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate desde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate hasta;

    private Long categoriaId;
}