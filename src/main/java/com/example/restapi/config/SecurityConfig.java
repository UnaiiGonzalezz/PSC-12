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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource())
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos (html, css, js, docs)
                .requestMatchers(
                    "/", "/index.html", "/login.html", "/registro.html",
                    "/cliente.html", "/compra.html", "/medicamento.html",
                    "/nueva-compra.html", "/ver-compra.html",
                    "/admin.html", "/admin-medicamentos.html",
                    "/favicon.ico", "/css/**", "/js/**",
                    "/swagger-ui/**", "/v3/api-docs/**", "/img/logo.png/**"
                ).permitAll()
                // Auth
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                // Medicamentos: GET público, resto solo ADMIN
                .requestMatchers(HttpMethod.GET, "/medicamentos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/medicamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/medicamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/medicamentos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/medicamentos/**").hasRole("ADMIN")
                // Compras: solo autenticado (usuarios logueados)
                .requestMatchers(HttpMethod.GET, "/compras/usuario/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/compras/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/compras/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/compras/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/compras/**").authenticated()
                // Si quieres, permite solo ADMIN el listado general de compras
                // .requestMatchers(HttpMethod.GET, "/compras").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS para desarrollo, añade/elimina orígenes según lo necesites
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "http://localhost:8080",
            "http://localhost:5173",
            "http://localhost:3000"
        ));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
