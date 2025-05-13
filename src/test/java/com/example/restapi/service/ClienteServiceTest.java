package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private CompraRepository compraRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarCliente_nuevoClienteGuardado() {
        RegistroDTO registro = new RegistroDTO();
        registro.setNombre("Carlos");
        registro.setApellido("Sánchez");
        registro.setEmail("carlos@example.com");
        registro.setContrasena("1234");
        registro.setTelefono("600123456");
        registro.setMetodoPago("Tarjeta");

        when(clienteRepo.existsByEmail("carlos@example.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("HASH1234");
        when(clienteRepo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente guardado = clienteService.registrarCliente(registro);

        assertThat(guardado.getEmail()).isEqualTo("carlos@example.com");
        assertThat(guardado.getRol()).isEqualTo("USER");
        verify(clienteRepo).save(any(Cliente.class));
    }

    @Test
    void verificarCredenciales_validas() {
        Cliente cliente = new Cliente("Ana", "López", "ana@example.com", "HASH", "600", "Tarjeta", "USER");

        when(clienteRepo.findByEmail("ana@example.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("1234", "HASH")).thenReturn(true);

        boolean ok = clienteService.verificarCredenciales("ana@example.com", "1234");

        assertThat(ok).isTrue();
    }

    @Test
    void verificarCredenciales_invalidas() {
        Cliente cliente = new Cliente("Ana", "López", "ana@example.com", "HASH", "600", "Tarjeta", "USER");

        when(clienteRepo.findByEmail("ana@example.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("wrong", "HASH")).thenReturn(false);

        boolean ok = clienteService.verificarCredenciales("ana@example.com", "wrong");

        assertThat(ok).isFalse();
    }

    @Test
    void getClienteByEmail_existente() {
        Cliente cliente = new Cliente("Ana", "López", "ana@example.com", "HASH", "600", "Tarjeta", "USER");

        when(clienteRepo.findByEmail("ana@example.com")).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.getClienteByEmail("ana@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("ana@example.com");
    }

    @Test
    void getClienteByEmail_noExiste_lanzaExcepcion() {
        when(clienteRepo.findByEmail("no@existe.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            clienteService.getClienteByEmail("no@existe.com");
        });
    }

    @Test
    void emailYaExiste_true() {
        String email = "ya@existe.com";
        when(clienteRepo.existsByEmail(email)).thenReturn(true);

        boolean result = clienteService.emailYaExiste(email);
        assertThat(result).isTrue();
    }

    @Test
    void emailYaExiste_false() {
        when(clienteRepo.existsByEmail("nuevo@correo.com")).thenReturn(false);
        assertThat(clienteService.emailYaExiste("nuevo@correo.com")).isFalse();
    }
}
