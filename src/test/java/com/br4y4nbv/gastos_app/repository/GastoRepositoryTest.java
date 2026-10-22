package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.*;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GastoRepositoryTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired GastoRepository gastoRepository;

    @Test
    void guardaYConsultaGastosDeUnUsuario() {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombre("Ana").email("ana@test.com").password("1234").build());

        Categoria comida = categoriaRepository.save(Categoria.builder()
                .nombre("Comida").usuario(usuario).build());

        gastoRepository.save(Gasto.builder()
                .descripcion("Almuerzo")
                .monto(new BigDecimal("25000.50"))
                .fecha(LocalDate.now())
                .categoria(comida)
                .usuario(usuario)
                .build());

        var resultado = gastoRepository.findByUsuarioId(usuario.getId(), PageRequest.of(0, 10));

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent().get(0).getDescripcion()).isEqualTo("Almuerzo");
    }
}
