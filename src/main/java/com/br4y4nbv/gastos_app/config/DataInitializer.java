package com.br4y4nbv.gastos_app.config;

import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Gasto;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import com.br4y4nbv.gastos_app.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    private static final String DEMO_EMAIL = "demo@gastos.com";

    // Cada fila: descripción, monto, categoría
    private static final List<Object[]> EJEMPLOS = List.of(
            new Object[]{"Almuerzo", "28000", "Comida"},
            new Object[]{"Supermercado", "135000", "Comida"},
            new Object[]{"Bus y metro", "18000", "Transporte"},
            new Object[]{"Gasolina", "90000", "Transporte"},
            new Object[]{"Internet", "75000", "Servicios"},
            new Object[]{"Energía", "110000", "Servicios"},
            new Object[]{"Cine", "32000", "Ocio"},
            new Object[]{"Cena con amigos", "64000", "Comida"});

    @Bean
    CommandLineRunner initData(UsuarioService usuarioService,
                               UsuarioRepository usuarios,
                               CategoriaRepository categorias,
                               GastoRepository gastos) {
        return args -> {
            Usuario demo = usuarios.findByEmail(DEMO_EMAIL)
                    .orElseGet(() -> usuarioService.registrar("Demo", DEMO_EMAIL, "demo1234"));

            // Solo siembra datos si el demo no tiene gastos
            if (!gastos.findTop5ByUsuarioIdOrderByFechaDescIdDesc(demo.getId()).isEmpty()) return;

            Map<String, Categoria> porNombre = categorias.findByUsuarioId(demo.getId()).stream()
                    .collect(Collectors.toMap(Categoria::getNombre, Function.identity()));

            LocalDate hoy = LocalDate.now();
            for (int i = 0; i < EJEMPLOS.size(); i++) {
                Object[] e = EJEMPLOS.get(i);
                // Fechas siempre dentro del mes actual, para que el dashboard no salga vacío
                LocalDate fecha = hoy.minusDays(i % hoy.getDayOfMonth());
                gastos.save(Gasto.builder()
                        .descripcion((String) e[0])
                        .monto(new BigDecimal((String) e[1]))
                        .fecha(fecha)
                        .categoria(porNombre.get((String) e[2]))
                        .usuario(demo)
                        .build());
            }
        };
    }
}