package com.br4y4nbv.gastos_app.service;

import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Prueba unitaria pura: sin Spring ni base de datos. Los repositorios son simulados.
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock CategoriaRepository categoriaRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UsuarioService usuarioService;

    @Test
    void registrarCifraLaPasswordNormalizaElEmailYCreaCategorias() {
        when(passwordEncoder.encode("clave1234")).thenReturn("HASH");
        // save() devuelve el mismo objeto que recibe
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario u = usuarioService.registrar("  Ana ", " ANA@Test.com ", "clave1234");

        assertThat(u.getNombre()).isEqualTo("Ana");
        assertThat(u.getEmail()).isEqualTo("ana@test.com");
        assertThat(u.getPassword()).isEqualTo("HASH");   // nunca se guarda en texto plano
        verify(categoriaRepository, times(4)).save(any(Categoria.class));
    }
}