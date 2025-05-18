package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CheckoutResponseDTO;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Tag("service")
@ExtendWith(MockitoExtension.class)
class ExtraTestsSuite {

    @InjectMocks
    private CarritoService carritoService;

    @Mock private CarritoRepository carritoRepo;
    @Mock private MedicamentoRepository medicamentoRepo;
    @Mock private StockMovimientoRepository movimientoRepo;
    @Mock private CompraService compraService;

    @InjectMocks
    private CompraService compraServiceReal;

    @Mock private CompraRepository compraRepo;

    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setup() {
        cliente = new Cliente("Eva", "Martín", "eva@demo.es", "HASH", "600123456", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 50, "Cinfa");
    }

    @Test
    void addMedicamento_carritoExistente() {
        Carrito carrito = new Carrito(cliente);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medicamentoRepo.findById(anyLong())).thenReturn(Optional.of(medicamento));

        var dto = carritoService.addMedicamento(cliente, 1L, 3);

        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getTotal()).isEqualTo(7.5);
    }

    @Test
    void checkout_carritoVacio_devuelveNull() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());

        CheckoutResponseDTO result = carritoService.checkout(cliente);

        assertThat(result).isNull();
    }

    @Test
    void crearDesdeCarrito_guardaCompraCorrectamente() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);

        Compra compra = new Compra(cliente, List.of(medicamento), LocalDate.now(), 1, "Tarjeta");
        when(compraRepo.save(any())).thenReturn(compra);

        Compra result = compraServiceReal.crearDesdeCarrito(carrito);
        assertThat(result.getCliente().getEmail()).isEqualTo("eva@demo.es");
    }

    @Test
    void getHistorialPorCliente_vacio() {
        when(compraRepo.findByClienteId(9L)).thenReturn(List.of());

        List<CompraResumenDTO> historial = compraServiceReal.getHistorialPorCliente(9L);
        assertThat(historial).isEmpty();
    }
}
