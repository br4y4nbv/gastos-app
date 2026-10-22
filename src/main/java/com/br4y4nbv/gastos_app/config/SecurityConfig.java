package com.br4y4nbv.gastos_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Clase que define configuración y "beans" (objetos gestionados por Spring)
public class SecurityConfig {

    // TEMPORAL: se reemplazara por el login real despues

    // @Bean: Spring ejecuta este método al arrancar y registra el resultado.
    // SecurityFilterChain define las reglas de seguridad HTTP.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // permitAll: cualquiera puede entrar a cualquier ruta (solo mientras se desarrolla)
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}