package com.example.restapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("user@mail.com", List.of("USER"));
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("user@mail.com");
        assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_USER");
    }

    @Test
    void testGenerateTokenWithPrefixedRole() {
        String token = jwtUtil.generateToken("prefixed@user.com", List.of("ROLE_ADMIN"));
        assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_ADMIN");
    }

    @Test
    void testInvalidToken() {
        String fakeToken = "abc.def.ghi";
        assertThat(jwtUtil.isTokenValid(fakeToken)).isFalse();
    }

    @Test
    void testExpiredTokenIsInvalid() {
        // Genera token expirado manualmente
        Key key = Keys.hmacShaKeyFor("e2ff2ab817d8430ea764af2ab817d8430ea764af2ab817d8430ea764af2ab817".getBytes());
        String token = Jwts.builder()
                .setSubject("expired@user.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // ya expirado
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        assertThat(jwtUtil.isTokenValid(token)).isFalse();
    }

    @Test
    void testMalformedToken() {
        String malformedToken = "malformed.token";
        assertThat(jwtUtil.isTokenValid(malformedToken)).isFalse();
    }
}
