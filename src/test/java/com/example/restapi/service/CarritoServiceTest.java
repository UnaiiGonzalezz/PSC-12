package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.CarritoRepository;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @InjectMocks
    private CarritoService service;

    @Mock private CarritoRepository        carritoRepo;
    @Mock private MedicamentoRepository    medRepo;
    @Mock private StockMovimientoRepository movRepo;
    @Mock private CompraService            compraService;  // usado en checkout

    private Cliente     ana;
    private Medicamento ibup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ana  = new Cliente("Ana","López","ana@demo.es","HASH","600","Tarjeta");
        ibup = new Medicamento("Ibuprofeno","Analgesico",5,20,"Bayer");
        ibup.setId(1L);
    }

    /* ---------- addMedicamento ---------- */
    @Test
    void addMedicamento_creaCarritoYDevuelveDTO() {
        // mock repos
        when(carritoRepo.findByCliente(ana)).thenReturn(Optional.empty());
        when(medRepo.findById(1L)).thenReturn(Optional.of(ibup));
        // carrito guardado
        ArgumentCaptor<Carrito> cap = ArgumentCaptor.forClass(Carrito.class);
        when(carritoRepo.save(any(Carrito.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var dto = service.addMedicamento(ana, 1L, 2);

        assertThat(dto.getTotal()).isEqualTo(10);   // 2 x 5
        verify(carritoRepo).save(cap.capture());
        assertThat(cap.getValue().getItems()).hasSize(1);
    }

    /* ---------- checkout ---------- */
    @Test
    void checkout_descuentaStockYCreaCompra() {
        // preparar carrito con ítem
        Carrito carrito = new Carrito(ana);
        carrito.addItem(ibup, 2);          // stock suficiente
        when(carritoRepo.findByCliente(ana)).thenReturn(Optional.of(carrito));

        // mocks repos
        when(medRepo.save(any(Medicamento.class))).thenAnswer(i -> i.getArgument(0));
        when(compraService.crearDesdeCarrito(any())).thenReturn(
                new Compra(ana, carrito.getItemsMedicamentos(),
                        carrito.getFecha(), 2, ana.getMetodoPago()));

        CheckoutResponseDTO dto = service.checkout(ana);

        assertThat(dto.getPago()).isEqualTo(10);
        verify(medRepo).save(argThat(m -> m.getStock() == 18));
        verify(carritoRepo).delete(carrito);              // carrito vacío
        verify(movRepo, atLeastOnce()).save(any(StockMovimiento.class));
    }
}
