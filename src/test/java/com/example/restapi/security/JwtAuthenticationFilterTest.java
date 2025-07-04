package com.example.restapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("security")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain chain;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_publicPath_sinToken() throws Exception {
        when(request.getServletPath()).thenReturn("/index.html");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilter_conTokenValido_autenticado() throws Exception {
        String token = "valid.jwt.token";

        when(request.getServletPath()).thenReturn("/medicamentos/8");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.isTokenValid(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn("admin@demo.es");
        when(jwtUtil.extractRoles(token)).thenReturn(List.of("ROLE_ADMIN"));

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(((User) auth.getPrincipal()).getUsername()).isEqualTo("admin@demo.es");
        assertThat(auth.getAuthorities())
            .extracting(Object::toString)
            .containsExactly("ROLE_ADMIN");
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
    }

    @Test
    void doFilter_conTokenInvalido_noAutenticado() throws Exception {
        when(request.getServletPath()).thenReturn("/medicamentos/8");
        when(request.getHeader("Authorization")).thenReturn("Bearer bad.token");
        when(jwtUtil.isTokenValid(any())).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
