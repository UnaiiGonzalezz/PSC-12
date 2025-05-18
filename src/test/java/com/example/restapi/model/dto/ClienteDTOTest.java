package com.example.restapi.model.dto;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ClienteDTOTest {

    @Test
    void testConstructorAndGetters() {
        ClienteDTO dto = new ClienteDTO(1L, "Juan", "Pérez", "juan@example.com", "TARJETA", "ADMIN");

        assertEquals(1L, dto.getId());
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellido());
        assertEquals("juan@example.com", dto.getEmail());
        assertEquals("TARJETA", dto.getMetodoPago());
        assertEquals("ADMIN", dto.getRol());
    }

    @Test
    void testSetters() {
        ClienteDTO dto = new ClienteDTO();

        dto.setId(2L);
        dto.setNombre("Ana");
        dto.setApellido("López");
        dto.setEmail("ana@example.com");
        dto.setMetodoPago("EFECTIVO");
        dto.setRol("USER");

        assertEquals(2L, dto.getId());
        assertEquals("Ana", dto.getNombre());
        assertEquals("López", dto.getApellido());
        assertEquals("ana@example.com", dto.getEmail());
        assertEquals("EFECTIVO", dto.getMetodoPago());
        assertEquals("USER", dto.getRol());
    }
}
