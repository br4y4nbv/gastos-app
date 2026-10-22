package com.br4y4nbv.gastos_app.config;

import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//DATOS de prueba
@Configuration
public class DataInitializer {

    // CommandLineRunner: código que se ejecuta UNA vez justo después de arrancar la app.
    // Spring inyecta los repositorios como parámetros del método.
    @Bean
    CommandLineRunner initData(UsuarioRepository usuarios, CategoriaRepository categorias) {
        return args -> {
            // Si ya hay usuarios, no hace nada (evita duplicar datos)
            if (usuarios.count() > 0) return;

            Usuario demo = usuarios.save(Usuario.builder()
                    .nombre("Demo").email("demo@gastos.com").password("temporal").build());

            // Crea 4 categorías para el usuario demo
            for (String nombre : new String[]{"Comida", "Transporte", "Servicios", "Ocio"}) {
                categorias.save(Categoria.builder().nombre(nombre).usuario(demo).build());
            }
        };
    }
}