package com.example.restapi.config;

import com.example.restapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Públicas
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/registro.html",
                    "/favicon.ico",
                    "/css/**",
                    "/js/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/admin.html",
                    "/admin-medicamentos.html",
                    "/cliente.html"
                ).permitAll()

                // Login y registro
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                // Medicamentos públicos
                .requestMatchers(HttpMethod.GET, "/medicamentos/**").permitAll()

                // Protegidos
                .requestMatchers("/api/**").authenticated()

                // Medicamento CRUD solo admin
                .requestMatchers(HttpMethod.POST, "/medicamentos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medicamentos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/medicamentos/**").hasAuthority("ADMIN")

                // Todo lo demás requiere autenticación
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
