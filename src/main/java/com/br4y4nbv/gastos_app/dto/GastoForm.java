package com.br4y4nbv.gastos_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

// Este objeto representa los datos del FORMULARIO, no la tabla.
// Se separa de la entidad Gasto porque:
//  - el formulario envía solo un id de categoría, no el objeto completo
//  - evita problemas con las relaciones LAZY
//  - evita que el usuario modifique campos que no debería (como el dueño)
@Getter @Setter
public class GastoForm {

    private Long id;

    // "message" es el texto que verá el usuario si la validación falla
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    // El input type="date" del navegador envía el formato ISO: 2026-10-22.
    // Esta anotación le indica a Spring cómo convertir ese texto a LocalDate.
    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "Selecciona una categoría")
    private Long categoriaId; // Solo el id; el servicio busca la categoría real
}