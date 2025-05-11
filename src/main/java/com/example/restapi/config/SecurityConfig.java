package com.example.restapi.config;

import com.example.restapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity              // 🔧 ¡FUNDAMENTAL!
@EnableMethodSecurity
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
                // Recursos públicos
                .requestMatchers(
                    "/", "/index.html", "/login.html", "/registro.html",
                    "/cliente.html", "/compra.html", "/medicamento.html",
                    "/nueva-compra.html", "/ver-compra.html",
                    "/admin.html", "/admin-medicamentos.html",
                    "/favicon.ico", "/css/**", "/js/**",
                    "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()

                // Autenticación
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                // Lectura pública
                .requestMatchers(HttpMethod.GET, "/medicamentos/**").permitAll()

                // Requiere ADMIN
                .requestMatchers(HttpMethod.POST, "/medicamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medicamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/medicamentos/**").hasRole("ADMIN")

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
