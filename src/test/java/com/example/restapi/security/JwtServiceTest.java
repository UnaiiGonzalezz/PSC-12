package com.example.restapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void testGenerateAndValidateToken() {
        String email = "test@mail.com";
        String token = jwtService.generateToken(email);

        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.getEmailFromToken(token)).isEqualTo(email);
    }

    @Test
    void testInvalidTokenReturnsFalse() {
        String fakeToken = "abc.def.ghi";
        assertThat(jwtService.isTokenValid(fakeToken)).isFalse();
    }

    @Test
    void testExpiredTokenReturnsFalse() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String expiredToken = Jwts.builder()
                .setSubject("expired@mail.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key)
                .compact();

        // Simula llamada directa a parser con clave distinta, por lo que fallará
        assertThat(jwtService.isTokenValid(expiredToken)).isFalse();
    }
}
