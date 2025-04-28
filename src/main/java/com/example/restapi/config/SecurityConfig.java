package com.example.restapi.config;

import com.example.restapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", 
                    "/index.html", 
                    "/login.html",
                    "/admin.html",
                    "/cliente.html",
                    "/compra.html",
                    "/nueva-compra.html",
                    "/medicamento.html",
                    "/css/**", 
                    "/js/**", 
                    "/favicon.ico",
                    "/auth/**", 
                    "/v3/api-docs/**", 
                    "/swagger-ui/**"
                ).permitAll() // ✨ todos estos recursos accesibles sin token

                // Permitir acceso abierto a catálogo y búsquedas de medicamentos
                .requestMatchers(
                    "/medicamentos/pag",
                    "/medicamentos/nombre/**",
                    "/medicamentos/{id}",
                    "/medicamentos/disponibles"
                ).permitAll()

                // Proteger acciones sensibles de medicamentos (crear, actualizar, eliminar)
                .requestMatchers("/medicamentos/**").hasAuthority("ADMIN")

                // Proteger API de clientes
                .requestMatchers("/api/**").authenticated()

                // Cualquier otra ruta necesita autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
