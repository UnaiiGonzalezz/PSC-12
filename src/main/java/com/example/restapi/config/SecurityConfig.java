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
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que usas JWT
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Usamos sesión sin estado para JWT
            .authorizeHttpRequests(auth -> auth
                // Permitimos acceso a recursos estáticos y rutas públicas
                .requestMatchers(
                    "/",                       // Página de inicio
                    "/index.html",             
                    "/cliente.html",            
                    "/compra.html",            
                    "/nueva-compra.html",      
                    "/admin.html",
                    "/medicamento.html",              
                    "/css/**",                 
                    "/js/**",                  
                    "/auth/**",                // Rutas de autenticación (login, etc)
                    "/v3/api-docs/**",         // Swagger docs
                    "/swagger-ui.html",        // Swagger UI
                    "/swagger-ui/**",          // Swagger UI recursos
                    "/medicamentos/**",        // Acceso público para ver medicamentos 
                    "/api/clientes",           // Endpoint clientes
                    "/api/clientes/**"         // Detalles de cliente
                ).permitAll()
                // Requiere autenticación para todas las demás rutas
                .anyRequest().authenticated()
            )
            // Añadimos el filtro JWT para la validación de autenticación
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usamos BCrypt para encriptar contraseñas
    }
}
