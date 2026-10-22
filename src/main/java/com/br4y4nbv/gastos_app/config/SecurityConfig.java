package com.br4y4nbv.gastos_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // BCrypt cifra la contraseña con "sal" aleatoria: dos usuarios con la misma
    // contraseña generan hashes distintos, y el hash no se puede revertir.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/login", "/registro", "/error").permitAll()
                // Todo lo demás exige sesión iniciada
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")                // Nuestra página, no la de Spring
                .usernameParameter("email")         // El campo del formulario se llama "email"
                .defaultSuccessUrl("/gastos", true) // Tras entrar, siempre a /gastos
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll());
        return http.build();
    }
}