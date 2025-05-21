package com.example.restapi.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUtilTest {

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUserEmail_conUsuarioAutenticado() {
        String email = "test@mail.com";
        var auth = new UsernamePasswordAuthenticationToken(email, null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String resultado = SecurityUtil.getCurrentUserEmail();
        assertThat(resultado).isEqualTo(email);
    }

    @Test
    void testGetCurrentUserEmail_sinAutenticacion() {
        SecurityContextHolder.clearContext();
        String resultado = SecurityUtil.getCurrentUserEmail();
        assertThat(resultado).isNull();
    }

    @Test
    void testGetCurrentUserEmail_authenticationEsNull() {
        SecurityContextHolder.getContext().setAuthentication(null); // explícito
        String resultado = SecurityUtil.getCurrentUserEmail();
        assertThat(resultado).isNull();
    }

    @Test
    void testGetCurrentUserEmail_contextSinAuthenticationDevuelveNull() {
        // Asegura un contexto no nulo pero con auth null (caso distinto a clearContext)
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

        String resultado = SecurityUtil.getCurrentUserEmail();
        assertThat(resultado).isNull();
    }


}
