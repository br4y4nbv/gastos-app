package com.br4y4nbv.gastos_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
//Nombres de tabla en plural para evitar choques con palabras reservadas
@Table(name = "usuarios")
//Getter y Setter, no Data. genera equals y hascode que dan problemas con entidades JPA
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;
}
