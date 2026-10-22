package com.br4y4nbv.gastos_app.service;

import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final List<String> CATEGORIAS_INICIALES =
            List.of("Comida", "Transporte", "Servicios", "Ocio");

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(normalizar(email));
    }

    // Crea el usuario con la contraseña cifrada y sus categorías iniciales.
    // @Transactional: si algo falla a mitad, no queda un usuario sin categorías.
    @Transactional
    public Usuario registrar(String nombre, String email, String passwordPlano) {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombre(nombre.trim())
                .email(normalizar(email))
                .password(passwordEncoder.encode(passwordPlano))
                .build());

        CATEGORIAS_INICIALES.forEach(nombreCat ->
                categoriaRepository.save(Categoria.builder()
                        .nombre(nombreCat).usuario(usuario).build()));

        return usuario;
    }

    // Se guarda siempre en minúsculas para que "Ana@x.com" y "ana@x.com" sean el mismo usuario
    private String normalizar(String email) {
        return email.trim().toLowerCase();
    }
}