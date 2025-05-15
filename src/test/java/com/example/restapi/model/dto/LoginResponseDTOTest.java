package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        ClienteDTO cliente = new ClienteDTO(3L, "Mario", "Gómez", "mario@example.com", "TARJETA", "ADMIN");
        LoginResponseDTO dto = new LoginResponseDTO("abc.def.ghi", cliente);

        assertEquals("abc.def.ghi", dto.getToken());
        assertEquals(cliente, dto.getCliente());
    }

    @Test
    void testSetters() {
        ClienteDTO cliente = new ClienteDTO();
        LoginResponseDTO dto = new LoginResponseDTO();

        dto.setToken("xyz.123.jwt");
        dto.setCliente(cliente);

        assertEquals("xyz.123.jwt", dto.getToken());
        assertEquals(cliente, dto.getCliente());
    }
}
