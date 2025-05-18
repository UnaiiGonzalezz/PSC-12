package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.repository.*;
import com.example.restapi.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("service")
@ExtendWith(MockitoExtension.class)
public class ExtendedTestSuite {

    @InjectMocks private CarritoService carritoService;
    @Mock private CarritoRepository carritoRepo;
    @Mock private MedicamentoRepository medicamentoRepo;
    @Mock private StockMovimientoRepository movimientoRepo;
    @Mock private CompraService compraService;

    @InjectMocks private CompraService compraSvc;
    @Mock private CompraRepository compraRepo;

    @InjectMocks private ClienteService clienteService;
    @Mock private ClienteRepository clienteRepo;
    @Mock private PasswordEncoder passwordEncoder;

    private JwtUtil jwtUtil;
    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Eva", "Martín", "eva@demo.es", "HASH", "600123456", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 50, "Cinfa");
        jwtUtil = new JwtUtil();
    }

    // ====== CarritoService ======

    @Test
    void addMedicamento_itemExistente_incrementaCantidad() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medicamentoRepo.findById(1L)).thenReturn(Optional.of(medicamento));

        var dto = carritoService.addMedicamento(cliente, 1L, 2);

        assertThat(dto.getItems().get(0).getCantidad()).isEqualTo(3);
    }

    @Test
    void checkout_conItems_borraCarrito() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medicamentoRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        when(compraService.crearDesdeCarrito(carrito)).thenReturn(new Compra());

        var dto = carritoService.checkout(cliente);

        verify(carritoRepo).delete(carrito);
        assertThat(dto).isNotNull();
    }

    // ====== CompraService ======

    @Test
    void estadoCompraDTO_noExiste() {
        when(compraRepo.findById(10L)).thenReturn(Optional.empty());

        var result = compraSvc.getEstadoCompraDTO(10L);
        assertThat(result).isEmpty();
    }

    @Test
    void updateEstado_actualizaCorrectamente() {
        Compra compra = new Compra();
        compra.setEstado("Pendiente");
        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        when(compraRepo.save(any())).thenReturn(compra);

        var updated = compraSvc.updateEstado(1L, "Enviado");

        assertThat(updated.getEstado()).isEqualTo("Enviado");
    }

    // ====== ClienteService ======

    @Test
    void emailYaExiste_true() {
        when(clienteRepo.existsByEmail("existe@demo.com")).thenReturn(true);
        assertThat(clienteService.emailYaExiste("existe@demo.com")).isTrue();
    }

    @Test
    void getClienteByEmail_optionalVacio() {
        when(clienteRepo.findByEmail("no@existe.com")).thenReturn(Optional.empty());
        assertThat(clienteService.getClienteByEmail("no@existe.com")).isNull();
    }

    @Test
    void verificarCredenciales_falsas() {
        Cliente c = new Cliente("A", "B", "a@b.com", "HASHED", "123", "Tarjeta", "USER");
        when(clienteRepo.findByEmail("a@b.com")).thenReturn(Optional.of(c));
        when(passwordEncoder.matches("wrong", "HASHED")).thenReturn(false);

        boolean result = clienteService.verificarCredenciales("a@b.com", "wrong");
        assertThat(result).isFalse();
    }

    // ====== JwtUtil ======

    @Test
    void token_extraccionRolesPersonalizados() {
        String token = jwtUtil.generateToken("rol@demo.es", List.of("MANAGER"));

        assertThat(jwtUtil.extractRoles(token)).contains("ROLE_MANAGER");
    }

    @Test
    void token_expirado_devuelveInvalido() {
        // Token con expiración antigua
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjoxNjAwMDAwMDAwLCJyb2xlcyI6WyJST0xFX1VTRVIiXX0.dummy-signature";
        assertThat(jwtUtil.isTokenValid(expiredToken)).isFalse();
    }
}
