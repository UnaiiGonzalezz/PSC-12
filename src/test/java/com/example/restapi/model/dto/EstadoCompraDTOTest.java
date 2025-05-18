package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Tag(name = "unit")
class EstadoCompraDTOTest {

    @Test
    void testConstructorVacioYSetters() {
        EstadoCompraDTO dto = new EstadoCompraDTO();

        dto.setId(1L);
        dto.setEstado("ENVIADO");
        dto.setFechaCompra(LocalDate.of(2024, 5, 17));
        dto.setClienteId(101L);

        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo();
        cliente.setNombre("Carlos");
        cliente.setApellido("Ramírez");
        cliente.setEmail("carlos@mail.com");
        dto.setCliente(cliente);

        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo();
        med.setNombre("Amoxicilina");
        med.setPrecio(7.5);
        List<EstadoCompraDTO.MedicamentoInfo> medicamentos = new ArrayList<>();
        medicamentos.add(med);
        dto.setMedicamentos(medicamentos);

        assertEquals(1L, dto.getId());
        assertEquals("ENVIADO", dto.getEstado());
        assertEquals(LocalDate.of(2024, 5, 17), dto.getFechaCompra());
        assertEquals(101L, dto.getClienteId());
        assertEquals("Carlos", dto.getCliente().getNombre());
        assertEquals("Ramírez", dto.getCliente().getApellido());
        assertEquals("carlos@mail.com", dto.getCliente().getEmail());
        assertEquals(1, dto.getMedicamentos().size());
        assertEquals("Amoxicilina", dto.getMedicamentos().get(0).getNombre());
        assertEquals(7.5, dto.getMedicamentos().get(0).getPrecio());
    }

    @Test
    void testConstructorConArgumentos() {
        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo("Lucía", "Méndez", "lucia@mail.com");
        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo("Ibuprofeno", 5.75);
        List<EstadoCompraDTO.MedicamentoInfo> medicamentos = new ArrayList<>();
        medicamentos.add(med);

        EstadoCompraDTO dto = new EstadoCompraDTO(2L, "PAGADO", LocalDate.of(2024, 6, 1), 102L, cliente, medicamentos);

        assertEquals(2L, dto.getId());
        assertEquals("PAGADO", dto.getEstado());
        assertEquals(LocalDate.of(2024, 6, 1), dto.getFechaCompra());
        assertEquals(102L, dto.getClienteId());
        assertEquals("Lucía", dto.getCliente().getNombre());
        assertEquals("Méndez", dto.getCliente().getApellido());
        assertEquals("lucia@mail.com", dto.getCliente().getEmail());
        assertEquals(1, dto.getMedicamentos().size());
        assertEquals("Ibuprofeno", dto.getMedicamentos().get(0).getNombre());
        assertEquals(5.75, dto.getMedicamentos().get(0).getPrecio());
    }

    @Test
    void testClienteInfoIndependiente() {
        EstadoCompraDTO.ClienteInfo cliente = new EstadoCompraDTO.ClienteInfo("Ana", "Lopez", "ana@mail.com");
        assertEquals("Ana", cliente.getNombre());
        assertEquals("Lopez", cliente.getApellido());
        assertEquals("ana@mail.com", cliente.getEmail());

        cliente.setNombre("Elena");
        cliente.setApellido("Martírez");  // Corregido aquí para que coincida con el assert
        cliente.setEmail("elena@mail.com");

        assertEquals("Elena", cliente.getNombre());
        assertEquals("Martírez", cliente.getApellido());
        assertEquals("elena@mail.com", cliente.getEmail());
    }

    @Test
    void testMedicamentoInfoIndependiente() {
        EstadoCompraDTO.MedicamentoInfo med = new EstadoCompraDTO.MedicamentoInfo("Paracetamol", 2.25);
        assertEquals("Paracetamol", med.getNombre());
        assertEquals(2.25, med.getPrecio());

        med.setNombre("Naproxeno");
        med.setPrecio(3.5);

        assertEquals("Naproxeno", med.getNombre());
        assertEquals(3.5, med.getPrecio());
    }
}
