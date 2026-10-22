package com.br4y4nbv.gastos_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistroForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo no válido")
    private String email;

    // BCrypt solo procesa hasta 72 bytes, por eso el máximo
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "Entre 8 y 72 caracteres")
    private String password;

    @NotBlank(message = "Confirma la contraseña")
    private String confirmarPassword;
}