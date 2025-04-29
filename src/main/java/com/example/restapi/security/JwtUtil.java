package com.example.restapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final String SECRET = "e2ff2ab817d8430ea764af2ab817d8430ea764af2ab817d8430ea764af2ab817"; // mínimo 256 bits
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 hora

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //  Generar token (con roles como lista)
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extraer email del token
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    //  Extraer lista de roles del token
    public List<String> extractRoles(String token) {
        Claims claims = parseClaims(token);
        return claims.get("roles", List.class);
    }

    // Verificar validez del token
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //  Parsear claims del token
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
