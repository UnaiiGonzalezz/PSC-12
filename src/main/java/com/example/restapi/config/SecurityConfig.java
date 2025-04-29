package com.example.restapi.config;

import com.example.restapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                // Rutas públicas
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/registro.html",
                    "/compra.html",
                    "/nueva-compra.html",
                    "/css/**",
                    "/js/**",
                    "/favicon.ico",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // API de login y registro públicas
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                // Catálogo de medicamentos público (GET)
                .requestMatchers(HttpMethod.GET,
                    "/medicamentos/pag",
                    "/medicamentos/nombre/**",
                    "/medicamentos/{id}",
                    "/medicamentos/disponibles"
                ).permitAll()

                // Páginas de administración: deben ser públicas para cargar HTML, controladas por JS
                .requestMatchers("/admin.html", "/admin-medicamentos.html", "/cliente.html").permitAll()

                // API protegida
                .requestMatchers("/api/**").authenticated()

                // Operaciones CRUD de medicamentos solo ADMIN
                .requestMatchers(HttpMethod.POST, "/medicamentos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medicamentos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/medicamentos/**").hasAuthority("ADMIN")

                // Todo lo demás: autenticado
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
