package com.br4y4nbv.gastos_app.repository;

import com.br4y4nbv.gastos_app.model.*;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
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

    @Test
    void filtraPorCategoriaYRangoDeFechas() {
        Usuario usuario = usuarioRepository.save(Usuario.builder()
                .nombre("Luis").email("luis@test.com").password("1234").build());
        Categoria comida = categoriaRepository.save(Categoria.builder()
                .nombre("Comida").usuario(usuario).build());
        Categoria ocio = categoriaRepository.save(Categoria.builder()
                .nombre("Ocio").usuario(usuario).build());

        gastoRepository.save(Gasto.builder().descripcion("Almuerzo").monto(new BigDecimal("20000"))
                .fecha(LocalDate.of(2026, 10, 5)).categoria(comida).usuario(usuario).build());
        gastoRepository.save(Gasto.builder().descripcion("Cine").monto(new BigDecimal("15000"))
                .fecha(LocalDate.of(2026, 10, 10)).categoria(ocio).usuario(usuario).build());
        gastoRepository.save(Gasto.builder().descripcion("Cena").monto(new BigDecimal("30000"))
                .fecha(LocalDate.of(2026, 9, 20)).categoria(comida).usuario(usuario).build());

        var spec = GastoSpecs.filtrar(usuario.getId(),
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), comida.getId());

        Page<Gasto> resultado = gastoRepository.findAll(spec, PageRequest.of(0, 10));

        assertThat(resultado.getTotalElements()).isEqualTo(1);
        assertThat(resultado.getContent().get(0).getDescripcion()).isEqualTo("Almuerzo");
    }
}
