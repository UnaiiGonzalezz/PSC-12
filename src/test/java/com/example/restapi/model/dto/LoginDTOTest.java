package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDTOTest {

    @Test
    void testConstructorAndGetters() {
        LoginDTO dto = new LoginDTO("usuario@mail.com", "1234");

        assertEquals("usuario@mail.com", dto.getEmail());
        assertEquals("1234", dto.getPassword());
    }

    @Test
    void testSetters() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("otra@mail.com");
        dto.setPassword("abcd");

        assertEquals("otra@mail.com", dto.getEmail());
        assertEquals("abcd", dto.getPassword());
    }
}
