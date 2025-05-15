package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoCompletoDTOTest {

    @Test
    void testConstructorAndGetters() {
        MedicamentoCompletoDTO dto = new MedicamentoCompletoDTO(
                10L, "Paracetamol", 5.5, "Analgésico", 100, "Distribuidora S.A.", true
        );

        assertEquals(10L, dto.getId());
        assertEquals("Paracetamol", dto.getNombre());
        assertEquals(5.5, dto.getPrecio());
        assertEquals("Analgésico", dto.getCategoria());
        assertEquals(100, dto.getStock());
        assertEquals("Distribuidora S.A.", dto.getProveedor());
        assertTrue(dto.isDisponible());
    }
}
