package com.br4y4nbv.gastos_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

//Big Decimal, para el dinero, nunca double. Evita errores de redondeo
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
//Nombres de tabla en plural para evitar choques con palabras reservadas
@Table(name = "gastos")
//Getter y Setter, no Data. genera equals y hascode que dan problemas con entidades JPA
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String descripcion;

    @NotNull
    @Positive
    @Column(precision = 12, scale = 2)
    private BigDecimal monto;

    @NotNull
    private LocalDate fecha;

    //FetcgType.LAZY para no cargar datos innecesarios
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
