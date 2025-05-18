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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Tag("service")
@ExtendWith(MockitoExtension.class)
public class ExtraCoverageTests {

    @InjectMocks
    private CarritoService carritoService;

    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private MedicamentoRepository medicamentoRepo;

    @Mock
    private StockMovimientoRepository movimientoRepo;

    @Mock
    private CompraService compraService;

    @InjectMocks
    private CompraService compraSvc;

    @Mock
    private CompraRepository compraRepo;

    private JwtUtil jwtUtil;
    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Eva", "Martín", "eva@demo.es", "HASH", "600123456", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 50, "Cinfa");
        jwtUtil = new JwtUtil();
    }

    @Test
    void test_addMedicamento_enCarritoExistente() {
        Carrito carrito = new Carrito(cliente);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medicamentoRepo.findById(anyLong())).thenReturn(Optional.of(medicamento));

        var dto = carritoService.addMedicamento(cliente, 1L, 2);

        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getTotal()).isEqualTo(5.0);
    }

    @Test
    void test_checkout_sinCarrito_lanzaExcepcion() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> carritoService.checkout(cliente));
    }

    @Test
    void test_getHistorialPorCliente_vacio() {
        when(compraRepo.findByClienteId(99L)).thenReturn(Collections.emptyList());

        List<CompraResumenDTO> result = compraSvc.getHistorialPorCliente(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void test_crearDesdeCarrito() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);

        when(compraRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Compra compra = compraSvc.crearDesdeCarrito(carrito);

        assertThat(compra.getCliente()).isEqualTo(cliente);
    }

    @Test
    void test_generateToken_y_extract() {
        String token = jwtUtil.generateToken("demo@user.com", List.of("ADMIN"));

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("demo@user.com");
        assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_ADMIN");
    }

    @Test
    void test_tokenInvalido_retornaFalse() {
        String tokenCorrupto = "bad.token.here";

        assertThat(jwtUtil.isTokenValid(tokenCorrupto)).isFalse();
    }
}
