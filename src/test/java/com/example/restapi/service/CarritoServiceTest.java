package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CheckoutResponseDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("service")
class CarritoServiceTest {

    @InjectMocks
    private CarritoService service;

    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private MedicamentoRepository medRepo;

    @Mock
    private StockMovimientoRepository movRepo;

    @Mock
    private CompraService compraService;

    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Juan", "Pérez", "juan@example.com", "HASH", "600000000", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 3.0, 50, "Bayer");
        medicamento.setId(1L);
    }

    @Test
    void testAddMedicamento_creaCarritoNuevo() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());
        when(medRepo.findById(1L)).thenReturn(Optional.of(medicamento));
        when(carritoRepo.save(any())).thenAnswer(inv -> {
            Carrito c = inv.getArgument(0);
            c.addItem(medicamento, 2);
            return c;
        });

        var dto = service.addMedicamento(cliente, 1L, 2);

        assertThat(dto).isNotNull();
        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getTotal()).isEqualTo(6.0);
    }

    @Test
    void testAddMedicamento_yaExisteEnCarrito_incrementaCantidad() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);

        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medRepo.findById(1L)).thenReturn(Optional.of(medicamento));

        var dto = service.addMedicamento(cliente, 1L, 2);

        assertThat(dto.getItems().get(0).getCantidad()).isEqualTo(3);
        assertThat(dto.getTotal()).isEqualTo(9.0);
    }

    @Test
    void testCheckout_sinCarrito_lanzaExcepcion() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> service.checkout(cliente));
    }

    @Test
    void testCheckout_medicamentoNoDisponible() {
        medicamento.setDisponible(false);
        medicamento.setStock(10);
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);

        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(compraService.crearDesdeCarrito(any())).thenThrow(new IllegalStateException("Medicamento no disponible"));

        assertThrows(IllegalStateException.class, () -> service.checkout(cliente));
    }

    @Test
    void testCheckout_stockInsuficiente() {
        medicamento.setStock(0);
        medicamento.setDisponible(true);
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));

        assertThrows(IllegalStateException.class, () -> service.checkout(cliente));
    }

    @Test
    void testCheckout_conItems_procesaCorrectamente() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);

        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Compra compra = new Compra(cliente, carrito.getItems().stream().map(CarritoItem::getMedicamento).toList(), LocalDate.now(), 2, "Tarjeta");
        compra.setPago(6.0);
        when(compraService.crearDesdeCarrito(carrito)).thenReturn(compra);

        CheckoutResponseDTO dto = service.checkout(cliente);

        assertThat(dto).isNotNull();
        assertThat(dto.getTotal()).isEqualTo(6.0);
        verify(movRepo, atLeastOnce()).save(any(StockMovimiento.class));
        verify(carritoRepo).delete(carrito);
    }

    @Test
    void testRemoveMedicamento() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medRepo.findById(1L)).thenReturn(Optional.of(medicamento));
        service.removeMedicamento(cliente, 1L);
        assertThat(carrito.getItems()).isEmpty();
    }

    @Test
    void testVaciarCarrito() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));

        service.vaciarCarrito(cliente);

        verify(carritoRepo).delete(carrito);
    }

    @Test
    void testGetTotal() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        double total = service.getTotal(cliente);
        assertThat(total).isEqualTo(6.0);
    }

    @Test
    void testGetCarritoDTO() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        assertThat(service.getCarritoDTO(cliente)).isNotNull();
    }

    @Test
    void testGetOrCreateCarrito() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());
        when(carritoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Carrito carrito = service.getOrCreateCarrito(cliente);
        assertThat(carrito).isNotNull();
    }
}
