package com.example.restapi.service;

import com.example.restapi.model.Carrito;
import com.example.restapi.model.Cliente;
import com.example.restapi.model.Medicamento;
import com.example.restapi.repository.CarritoRepository;
import com.example.restapi.repository.CompraRepository;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarritoServiceExtraTest {

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

    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Eva", "Martín", "eva@demo.es", "HASH", "600123456", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 50, "Cinfa");
    }

    @Test
    void addMedicamento_carritoExistente() {
        Carrito carrito = new Carrito(cliente);
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.of(carrito));
        when(medicamentoRepo.findById(1L)).thenReturn(Optional.of(medicamento));

        var dto = carritoService.addMedicamento(cliente, 1L, 3);

        assertThat(dto.getItems()).hasSize(1);
        assertThat(dto.getTotal()).isEqualTo(7.5);
    }

    @Test
    void checkout_carritoNoExiste_lanzaExcepcion() {
        when(carritoRepo.findByCliente(cliente)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            carritoService.checkout(cliente);
        });
    }
}
