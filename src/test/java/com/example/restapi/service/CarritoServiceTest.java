package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CheckoutResponseDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.CarritoRepository;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    private Cliente ana;
    private Medicamento ibup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ana = new Cliente("Ana", "López", "ana@demo.es", "HASH", "600", "Tarjeta", "USER");
        ibup = new Medicamento("Ibuprofeno", "Analgésico", 5.0, 20, "Bayer");
        ibup.setId(1L);
    }

    @Test
    void addMedicamento_creaCarritoYDevuelveDTO() {
        when(carritoRepo.findByCliente(ana)).thenReturn(Optional.empty());
        when(medRepo.findById(1L)).thenReturn(Optional.of(ibup));
        when(carritoRepo.save(any(Carrito.class))).thenAnswer(invocation -> {
            Carrito carrito = invocation.getArgument(0);
            carrito.addItem(ibup, 2);
            return carrito;
        });

        var dto = service.addMedicamento(ana, 1L, 2);

        assertThat(dto.getTotal()).isEqualTo(10.0);
    }

    @Test
    void checkout_descuentaStockYCreaCompra() {
        Carrito carrito = new Carrito(ana);
        carrito.addItem(ibup, 2);

        when(carritoRepo.findByCliente(ana)).thenReturn(Optional.of(carrito));
        when(medRepo.save(any(Medicamento.class))).thenAnswer(i -> i.getArgument(0));

        Compra compra = new Compra(ana, carrito.getItems().stream().map(CarritoItem::getMedicamento).toList(), LocalDate.now(), 2, ana.getMetodoPago());
        compra.setPago(10.0);

        when(compraService.crearDesdeCarrito(any())).thenReturn(compra);

        CheckoutResponseDTO dto = service.checkout(ana);

        assertThat(dto.getTotal()).isEqualTo(10.0);
        verify(medRepo).save(argThat(m -> m.getStock() == 18));
        verify(carritoRepo).delete(carrito);
        verify(movRepo, atLeastOnce()).save(any(StockMovimiento.class));
    }
}
