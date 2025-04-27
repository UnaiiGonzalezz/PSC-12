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
        verify(clienteRepo).save(any(Cliente.class));
    }

    @Test
    void verificarCredenciales_contraseñaCorrecta() {
        Cliente cliente = new Cliente("Ana", "López", "ana@example.com", "HASHED_PASS", "600", "Tarjeta");

        when(clienteRepo.findByEmail("ana@example.com")).thenReturn(Optional.of(cliente)); // <--- corregido aquí
        when(passwordEncoder.matches("1234", "HASHED_PASS")).thenReturn(true);

        boolean resultado = clienteService.verificarCredenciales("ana@example.com", "1234");

        assertThat(resultado).isTrue();
    }
}
