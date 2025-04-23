package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService service;

    @Mock private ClienteRepository clienteRepo;
    @Mock private CompraRepository  compraRepo;
    @Mock private PasswordEncoder   encoder;

    private RegistroDTO registroDTO;
    private Cliente     clienteGuardado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registroDTO = new RegistroDTO();
        registroDTO.setNombre("Ana");
        registroDTO.setApellido("López");
        registroDTO.setEmail("ana@demo.es");
        registroDTO.setContrasena("1234");
        registroDTO.setTelefono("600000000");
        registroDTO.setMetodoPago("Tarjeta");

        clienteGuardado = new Cliente("Ana","López",
                "ana@demo.es","HASH", "600000000","Tarjeta");
    }

    /* ---------- registrarCliente ---------- */

    @Test
    void registraCliente_ok() {
        when(clienteRepo.existsByEmail("ana@demo.es")).thenReturn(false);
        when(encoder.encode("1234")).thenReturn("HASH");
        when(clienteRepo.save(any())).thenReturn(clienteGuardado);

        Cliente creado = service.registrarCliente(registroDTO);

        assertThat(creado.getEmail()).isEqualTo("ana@demo.es");
        verify(clienteRepo).save(any());
    }

    @Test
    void registraCliente_emailDuplicado_lanzaExcepcion() {
        when(clienteRepo.existsByEmail("ana@demo.es")).thenReturn(true);
        assertThatThrownBy(() -> service.registrarCliente(registroDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
    }

    /* ---------- verificarCredenciales ---------- */

    @Test
    void credencialesCorrectas() {
        when(clienteRepo.findByEmail("ana@demo.es")).thenReturn(clienteGuardado);
        when(encoder.matches("1234", "HASH")).thenReturn(true);

        boolean ok = service.verificarCredenciales("ana@demo.es","1234");
        assertThat(ok).isTrue();
    }

    @Test
    void credencialesIncorrectas() {
        when(clienteRepo.findByEmail("ana@demo.es")).thenReturn(clienteGuardado);
        when(encoder.matches("mala", "HASH")).thenReturn(false);

        boolean ok = service.verificarCredenciales("ana@demo.es","mala");
        assertThat(ok).isFalse();
    }

    /* ---------- obtenerComprasPorCliente ---------- */

    @Test
    void obtieneComprasPorCliente() {
        when(compraRepo.findByClienteId(1L)).thenReturn(List.of(new Compra()));
        List<Compra> lista = service.obtenerComprasPorCliente(1L);
        assertThat(lista).hasSize(1);
    }
}
