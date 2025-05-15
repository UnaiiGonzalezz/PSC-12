package com.example.restapi.model.dto;
import java.util.List;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class EstadoCompraDTOTest {

    @Test
    void testMainDTO() {
        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo("Luis", "Pérez", "luis@mail.com");
        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo("Ibuprofeno", 4.25);

        EstadoCompraDTO dto = new EstadoCompraDTO(9L, "PAGADO", LocalDate.of(2024, 6, 10), 3L, cliente, Collections.singletonList(med));

        assertEquals(9L, dto.getId());
        assertEquals("PAGADO", dto.getEstado());
        assertEquals(LocalDate.of(2024, 6, 10), dto.getFechaCompra());
        assertEquals(3L, dto.getClienteId());
        assertEquals(cliente, dto.getCliente());
        assertEquals(1, dto.getMedicamentos().size());
        assertEquals(med, dto.getMedicamentos().get(0));
    }

    @Test
    void testClienteInfo() {
        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo();
        cliente.setNombre("Ana");
        cliente.setApellido("Gómez");
        cliente.setEmail("ana@example.com");

        assertEquals("Ana", cliente.getNombre());
        assertEquals("Gómez", cliente.getApellido());
        assertEquals("ana@example.com", cliente.getEmail());
    }

    @Test
    void testMedicamentoInfo() {
        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo();
        med.setNombre("Paracetamol");
        med.setPrecio(2.5);

        assertEquals("Paracetamol", med.getNombre());
        assertEquals(2.5, med.getPrecio());
    }

    @Test
    void testConstructorCompleto() {
        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo("A", "B", "a@b.com");
        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo("X", 1.0);
        EstadoCompraDTO dto = new EstadoCompraDTO(1L, "ENTREGADO", LocalDate.of(2024, 5, 15), 7L, cliente, List.of(med));

        assertNotNull(dto);
    }
}
