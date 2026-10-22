package com.br4y4nbv.gastos_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
//Nombres de tabla en plural para evitar choques con palabras reservadas
@Table(name = "categorias")
//Getter y Setter, no Data. genera equals y hascode que dan problemas con entidades JPA
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    //FetcgType.LAZY para no cargar datos innecesarios
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}   
