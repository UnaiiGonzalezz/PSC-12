package com.example.restapi.service;

import com.example.restapi.model.*;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    }

    @Test
    void testGetHistorialPorCliente_sinCompras() {
        when(compraRepo.findByClienteId(2L)).thenReturn(Collections.emptyList());
        List<CompraResumenDTO> result = service.getHistorialPorCliente(2L);
        assertThat(result).isEmpty();
    }

    @Test
    void testCrearDesdeCarrito() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 2);
        when(compraRepo.save(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));
        Compra result = service.crearDesdeCarrito(carrito);
        assertThat(result.getMedicamentos()).hasSize(1);
    }

    @Test
    void testUpdateEstado_existente() {
        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        when(compraRepo.save(any())).thenReturn(compra);
        Compra result = service.updateEstado(1L, "Entregado");
        assertThat(result.getEstado()).isEqualTo("Entregado");
    }

    @Test
    void testUpdateEstado_noExiste() {
        when(compraRepo.findById(2L)).thenReturn(Optional.empty());
        Compra result = service.updateEstado(2L, "Recibido");
        assertThat(result).isNull();
    }

    @Test
    void updateEstado_estadoInvalido_lanzaExcepcion() {
        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        assertThrows(RuntimeException.class, () -> service.updateEstado(1L, "Desconocido"));
    }

    @Test
    void testGetEstadoCompraDTO_existente() {
        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        Optional<EstadoCompraDTO> result = service.getEstadoCompraDTO(1L);
        assertThat(result).isPresent();
    }

    @Test
    void testGetEstadoCompraDTO_noExiste() {
        when(compraRepo.findById(2L)).thenReturn(Optional.empty());
        Optional<EstadoCompraDTO> result = service.getEstadoCompraDTO(2L);
        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllCompras() {
        when(compraRepo.findAll()).thenReturn(List.of(new Compra(), new Compra()));
        assertThat(service.getAllCompras()).hasSize(2);
    }

    @Test
    void testGetCompraById() {
        when(compraRepo.findById(1L)).thenReturn(Optional.of(compra));
        assertThat(service.getCompraById(1L)).isPresent();
    }

    @Test
    void testGetComprasByEstado() {
        when(compraRepo.findByEstado("Pendiente")).thenReturn(List.of(compra));
        assertThat(service.getComprasByEstado("Pendiente")).hasSize(1);
    }

    @Test
    void testSaveCompra() {
        when(compraRepo.save(compra)).thenReturn(compra);
        assertThat(service.saveCompra(compra)).isEqualTo(compra);
    }

    @Test
    void testDeleteCompra() {
        service.deleteCompra(1L);
        verify(compraRepo).deleteById(1L);
    }
}
