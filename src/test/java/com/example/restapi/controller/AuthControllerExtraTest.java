package com.example.restapi.controller;
import org.springframework.context.annotation.Import;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.springframework.context.annotation.Import;
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
import static org.mockito.Mockito.*;

public class AuthControllerExtraTest {

    @InjectMocks
    private AuthController controller;

    @Mock
    private ClienteService clienteService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_tokenGenerado_noEsNull() {
        LoginDTO login = new LoginDTO("user@test.com", "pass");
        Cliente cliente = new Cliente("Test", "User", login.getEmail(), "HASH", "600", "Tarjeta", "USER");

        when(clienteService.verificarCredenciales(login.getEmail(), login.getPassword())).thenReturn(true);
        when(clienteService.getClienteByEmail(login.getEmail())).thenReturn(cliente);
        when(jwtUtil.generateToken(eq(cliente.getEmail()), any())).thenReturn("TOKEN123");

        ResponseEntity<?> res = controller.login(login);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void register_returnStatusConflictIfEmailExists() {
        RegistroDTO dto = new RegistroDTO();
        dto.setEmail("existing@test.com");
        when(clienteService.emailYaExiste(dto.getEmail())).thenReturn(true);

        ResponseEntity<?> response = controller.register(dto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
