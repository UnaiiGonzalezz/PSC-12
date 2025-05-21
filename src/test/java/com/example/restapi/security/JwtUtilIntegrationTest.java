package com.example.restapi.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtUtilIntegrationTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void integrationTestGenerateValidateExtractClaims() {
        String token = jwtUtil.generateToken("integration@test.com", List.of("USER"));

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("integration@test.com");
        assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_USER");
    }
}
