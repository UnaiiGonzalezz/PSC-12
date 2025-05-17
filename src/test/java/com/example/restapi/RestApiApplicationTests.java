package com.example.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class RestApiApplicationTest {

    @Test
    void contextLoads() {
        // Verifica que el contexto Spring arranca sin errores
    }

    @Test
    void mainMethodRuns() {
        assertDoesNotThrow(() -> RestApiApplication.main(new String[]{}));
    }
}
