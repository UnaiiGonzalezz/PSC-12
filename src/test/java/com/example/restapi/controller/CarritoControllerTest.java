package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.testconfig.TestSecurityConfig;

import org.junit.jupiter.api.Tag;  // Import correcto para @Tag de JUnit 5
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(CarritoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("controller")  
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testObtenerContenidoCarrito() throws Exception {
        when(clienteService.getClienteByEmail(any())).thenReturn(new Cliente());

        mockMvc.perform(get("/carrito/testUser@example.com"))
               .andExpect(status().isOk());
    }
}
