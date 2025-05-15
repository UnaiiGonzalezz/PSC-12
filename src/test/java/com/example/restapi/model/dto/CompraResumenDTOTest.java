package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CompraResumenDTOTest {

    @Test
    void testConstructorAndGetters() {
        CompraResumenDTO dto = new CompraResumenDTO(5L, LocalDate.of(2024, 1, 1), "ENTREGADO", 55.75);

        assertEquals(5L, dto.getId());
        assertEquals(LocalDate.of(2024, 1, 1), dto.getFechaCompra());
        assertEquals("ENTREGADO", dto.getEstado());
        assertEquals(55.75, dto.getTotal());
    }

    @Test
    void testSetters() {
        CompraResumenDTO dto = new CompraResumenDTO();

        dto.setId(6L);
        dto.setFechaCompra(LocalDate.of(2025, 2, 2));
        dto.setEstado("CANCELADO");
        dto.setTotal(0.0);

        assertEquals(6L, dto.getId());
        assertEquals(LocalDate.of(2025, 2, 2), dto.getFechaCompra());
        assertEquals("CANCELADO", dto.getEstado());
        assertEquals(0.0, dto.getTotal());
    }
}
