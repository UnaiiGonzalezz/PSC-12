package com.example.restapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("security")
class JwtUtilExtraTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_y_extraerClaims() {
        String token = jwtUtil.generateToken("demo@user.com", List.of("ADMIN"));

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("demo@user.com");
        assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_ADMIN");
    }

    @Test
    void tokenInvalido_retornarFalse() {
        String corruptToken = "bad.token.value";

        assertThat(jwtUtil.isTokenValid(corruptToken)).isFalse();
    }
}
