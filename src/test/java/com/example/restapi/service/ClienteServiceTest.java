package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.CompraRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("service")
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
    void registrarCliente_emailExistente_sigueGuardandoCliente() {
        RegistroDTO dto = new RegistroDTO();
        dto.setNombre("Ana");
        dto.setApellido("López");
        dto.setEmail("repetido@example.com");
        dto.setContrasena("1234");
        dto.setTelefono("600123456");
        dto.setMetodoPago("Tarjeta");

        when(clienteRepo.existsByEmail("repetido@example.com")).thenReturn(true);
        when(passwordEncoder.encode("1234")).thenReturn("HASHED");
        when(clienteRepo.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente resultado = clienteService.registrarCliente(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getRol()).isEqualTo("USER");
        verify(clienteRepo).save(any());
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
    void verificarCredenciales_noExiste() {
        when(clienteRepo.findByEmail("no@existe.com")).thenReturn(Optional.empty());
        boolean ok = clienteService.verificarCredenciales("no@existe.com", "1234");
        assertThat(ok).isFalse();
    }

    @Test
    void getClienteByEmail_existente() {
        Cliente cliente = new Cliente();
        cliente.setEmail("ana@example.com");
        when(clienteRepo.findByEmail("ana@example.com")).thenReturn(Optional.of(cliente));
        Cliente result = clienteService.getClienteByEmail("ana@example.com");
        assertThat(result.getEmail()).isEqualTo("ana@example.com");
    }

    @Test
    void getClienteByEmail_noExiste() {
        when(clienteRepo.findByEmail("no@existe.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clienteService.getClienteByEmail("no@existe.com"));
    }

    @Test
    void emailYaExiste_true() {
        when(clienteRepo.existsByEmail("existente@correo.com")).thenReturn(true);
        assertThat(clienteService.emailYaExiste("existente@correo.com")).isTrue();
    }

    @Test
    void emailYaExiste_false() {
        when(clienteRepo.existsByEmail("nuevo@correo.com")).thenReturn(false);
        assertThat(clienteService.emailYaExiste("nuevo@correo.com")).isFalse();
    }

    @Test
    void deleteCliente_existente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(cliente));
        clienteService.deleteCliente(1L);
        verify(clienteRepo).delete(cliente);
    }

    @Test
    void deleteCliente_noExiste() {
        when(clienteRepo.findById(2L)).thenReturn(Optional.empty());
        clienteService.deleteCliente(2L);
        verify(clienteRepo, never()).delete(any());
    }

    @Test
    void updateCliente_existente() {
        Cliente original = new Cliente("Juan", "Pérez", "juan@correo.com", "HASH", "600", "Tarjeta", "USER");
        Cliente nuevo = new Cliente("Juan Actualizado", "Pérez", "juan@correo.com", "HASH", "600", "Efectivo", "USER");
        when(clienteRepo.findById(1L)).thenReturn(Optional.of(original));
        when(clienteRepo.save(original)).thenReturn(original);
        Cliente actualizado = clienteService.updateCliente(1L, nuevo);
        assertThat(actualizado.getNombre()).isEqualTo("Juan Actualizado");
    }

    @Test
    void updateCliente_noExiste() {
        Cliente nuevo = new Cliente();
        when(clienteRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> clienteService.updateCliente(99L, nuevo));
    }

    @Test
    void updateCliente_actualizaCamposCompletos() {
        Cliente original = new Cliente("Juan", "Pérez", "juan@correo.com", "HASH", "600", "Tarjeta", "USER");
        Cliente nuevo = new Cliente("Juan", "Gómez", "juan@correo.com", "HASH", "700", "Bizum", "USER");

        when(clienteRepo.findById(1L)).thenReturn(Optional.of(original));
        when(clienteRepo.save(original)).thenReturn(original);

        Cliente actualizado = clienteService.updateCliente(1L, nuevo);

        assertThat(actualizado.getApellido()).isEqualTo("Gómez");
        assertThat(actualizado.getTelefono()).isEqualTo("700");
        assertThat(actualizado.getMetodoPago()).isEqualTo("Bizum");
    }

    @Test
    void findByEmail_existente() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");

        when(clienteRepo.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
        Optional<Cliente> result = clienteService.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail_noExiste() {
        when(clienteRepo.findByEmail("no@correo.com")).thenReturn(Optional.empty());
        Optional<Cliente> result = clienteService.findByEmail("no@correo.com");
        assertThat(result).isEmpty();
    }

    @Test
    void save_clienteConPasswordEnTextoPlano_loGuardaConHash() {
        Cliente cliente = new Cliente();
        cliente.setContrasena("1234");

        when(passwordEncoder.encode("1234")).thenReturn("HASHED");
        when(clienteRepo.save(cliente)).thenReturn(cliente);

        Cliente guardado = clienteService.save(cliente);
        assertThat(guardado.getContrasena()).isEqualTo("HASHED");
        verify(clienteRepo).save(cliente);
    }

    



}
