package com.br4y4nbv.gastos_app.config;

import com.br4y4nbv.gastos_app.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    // Usuario de demostración para que quien revise el proyecto pueda entrar sin registrarse.
    // Su contraseña es pública a propósito: no uses datos reales en la demo.
    @Bean
    CommandLineRunner initData(UsuarioService usuarioService) {
        return args -> {
            if (!usuarioService.existeEmail("demo@gastos.com")) {
                usuarioService.registrar("Demo", "demo@gastos.com", "demo1234");
            }
        };
    }
}