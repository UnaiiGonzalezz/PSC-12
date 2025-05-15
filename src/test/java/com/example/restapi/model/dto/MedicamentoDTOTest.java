package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoDTOTest {

    @Test
    void testConstructorAndGetters() {
        MedicamentoDTO dto = new MedicamentoDTO(1L, "Ibuprofeno", 3.5, "Analgésico", 50, "ProveedorX", true);

        assertEquals(1L, dto.getId());
        assertEquals("Ibuprofeno", dto.getNombre());
        assertEquals(3.5, dto.getPrecio());
        assertEquals("Analgésico", dto.getCategoria());
        assertEquals(50, dto.getStock());
        assertEquals("ProveedorX", dto.getProveedor());
        assertTrue(dto.isDisponible());
    }

    @Test
    void testSetters() {
        MedicamentoDTO dto = new MedicamentoDTO();

        dto.setId(2L);
        dto.setNombre("Paracetamol");
        dto.setPrecio(2.99);
        dto.setCategoria("Antitérmico");
        dto.setStock(80);
        dto.setProveedor("Distribuidora Y");
        dto.setDisponible(false);

        assertEquals(2L, dto.getId());
        assertEquals("Paracetamol", dto.getNombre());
        assertEquals(2.99, dto.getPrecio());
        assertEquals("Antitérmico", dto.getCategoria());
        assertEquals(80, dto.getStock());
        assertEquals("Distribuidora Y", dto.getProveedor());
        assertFalse(dto.isDisponible());
    }
}
