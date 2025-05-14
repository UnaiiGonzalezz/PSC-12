package com.example.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // ✅ Usa el perfil de test para evitar MySQL, Flyway, seguridad, etc.
class RestApiApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Boot se carga sin errores
    }

}
