package com.example.restapi.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getServletPath();

        // ✅ Permitir rutas públicas sin validar token
        if (isPublicPath(path)) {
            chain.doFilter(req, res);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);
                List<String> roles = jwtUtil.extractRoles(token);

                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }

    private boolean isPublicPath(String path) {
        return path != null && (
            path.equals("/") ||
            path.startsWith("/auth") ||
            path.endsWith(".html") ||
            path.startsWith("/css/") ||
            path.startsWith("/js/") ||
            path.startsWith("/medicamentos/") ||
            path.startsWith("/swagger-ui/") ||
            path.startsWith("/v3/api-docs/") ||
            path.equals("/favicon.ico")
        );
    }
}
