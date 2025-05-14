package com.example.restapi.service;

import com.example.restapi.model.Carrito;
import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    @InjectMocks
    private CompraService service;

    @Mock
    private CompraRepository compraRepo;

    private Cliente cliente;
    private Medicamento medicamento;
    private Compra compra;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Ana", "López", "ana@demo.es", "HASH", "600", "Tarjeta", "USER");
        cliente.setId(1L);

        medicamento = new Medicamento("Ibuprofeno", "Analgésico", 5, 30, "Bayer");
        medicamento.setId(10L);

        compra = new Compra(cliente, List.of(medicamento), LocalDate.now(), 2, cliente.getMetodoPago());
        compra.setId(1L);
        compra.setEstado("Pendiente");
    }

    @Test
    void testGetHistorialPorCliente_conCompras() {
        when(compraRepo.findByClienteId(1L)).thenReturn(List.of(compra));

        List<CompraResumenDTO> result = service.getHistorialPorCliente(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo("Pendiente");

        verify(compraRepo).findByClienteId(1L);
    }

    @Test
    void testGetHistorialPorCliente_sinCompras() {
        when(compraRepo.findByClienteId(2L)).thenReturn(Collections.emptyList());

        List<CompraResumenDTO> result = service.getHistorialPorCliente(2L);

        assertThat(result).isEmpty();
        verify(compraRepo).findByClienteId(2L);
    }

    @Test
    void testCrearDesdeCarrito() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);

        when(compraRepo.save(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));

        Compra result = service.crearDesdeCarrito(carrito);

        assertThat(result.getCliente()).isEqualTo(cliente);
        assertThat(result.getMedicamentos()).hasSize(1);
        verify(compraRepo).save(any(Compra.class));
    }

    @Test
    void testUpdateEstado_existente() {
        compra.setEstado("Pendiente");

        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        when(compraRepo.save(any())).thenReturn(compra);

        Compra result = service.updateEstado(1L, "Entregado");

        assertThat(result.getEstado()).isEqualTo("Entregado");

        verify(compraRepo).findById(1L);
        verify(compraRepo).save(any());
    }

    @Test
    void testUpdateEstado_noExiste() {
        when(compraRepo.findById(2L)).thenReturn(Optional.empty());

        Compra result = service.updateEstado(2L, "Recibido");

        assertThat(result).isNull();
        verify(compraRepo).findById(2L);
        verify(compraRepo, never()).save(any());
    }

    @Test
    void testGetEstadoCompraDTO_existente() {
        EstadoCompraDTO.ClienteInfo clienteInfo = new EstadoCompraDTO.ClienteInfo(
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail()
        );

        EstadoCompraDTO.MedicamentoInfo medicamentoInfo = new EstadoCompraDTO.MedicamentoInfo(
                medicamento.getNombre(),
                medicamento.getPrecio()
        );

        EstadoCompraDTO esperado = new EstadoCompraDTO(
                compra.getId(),
                compra.getEstado(),
                compra.getFechaCompra(),
                cliente.getId(), // nuevo campo clienteId
                clienteInfo,
                List.of(medicamentoInfo)
        );

        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));

        Optional<EstadoCompraDTO> result = service.getEstadoCompraDTO(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getCliente().getEmail()).isEqualTo("ana@demo.es");
        assertThat(result.get().getClienteId()).isEqualTo(1L);

        verify(compraRepo).findById(1L);
    }

    @Test
    void testGetEstadoCompraDTO_noExiste() {
        when(compraRepo.findById(2L)).thenReturn(Optional.empty());

        Optional<EstadoCompraDTO> result = service.getEstadoCompraDTO(2L);

        assertThat(result).isEmpty();
        verify(compraRepo).findById(2L);
    }
}
