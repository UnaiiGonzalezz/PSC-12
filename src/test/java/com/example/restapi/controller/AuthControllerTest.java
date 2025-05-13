package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.LoginDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController controller;

    @Mock
    private ClienteService clienteService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_exitoso() {
        LoginDTO login = new LoginDTO("admin@demo.es", "1234");
        Cliente admin = new Cliente("Admin", "Sistema", login.getEmail(), "HASH", "000", "Tarjeta", "ADMIN");

        when(clienteService.verificarCredenciales(login.getEmail(), login.getPassword())).thenReturn(true);
        when(clienteService.getClienteByEmail(login.getEmail())).thenReturn(admin);
        when(jwtUtil.generateToken(eq(admin.getEmail()), any())).thenReturn("TOKEN");

        ResponseEntity<?> res = controller.login(login);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((String) ((java.util.Map<?, ?>) res.getBody()).get("token"))).isEqualTo("TOKEN");
    }

    @Test
    void login_fallido() {
        LoginDTO login = new LoginDTO("noexiste@demo.es", "badpass");
        when(clienteService.verificarCredenciales(any(), any())).thenReturn(false);
        ResponseEntity<?> res = controller.login(login);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void register_nuevoCliente() {
        RegistroDTO dto = new RegistroDTO();
        dto.setEmail("nuevo@demo.es");
        dto.setNombre("Nuevo");
        dto.setContrasena("1234");

        when(clienteService.emailYaExiste(dto.getEmail())).thenReturn(false);
        Cliente creado = new Cliente();
        creado.setId(99L);
        creado.setEmail(dto.getEmail());

        when(clienteService.save(any())).thenReturn(creado);

        ResponseEntity<?> res = controller.register(dto);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((java.util.Map<?, ?>) res.getBody()).get("email")).isEqualTo("nuevo@demo.es");
    }

    @Test
    void register_conflicto() {
        RegistroDTO dto = new RegistroDTO();
        dto.setEmail("ya@existe.com");
        when(clienteService.emailYaExiste(dto.getEmail())).thenReturn(true);
        ResponseEntity<?> res = controller.register(dto);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
