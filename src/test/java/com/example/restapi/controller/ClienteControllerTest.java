package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.ClienteDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.testconfig.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("controller")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllClientes() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cliente"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetClienteById_found() throws Exception {
        Cliente cliente = new Cliente("Ana", "López", "ana@mail.com", "123", "600", "Tarjeta", "USER");
        cliente.setId(1L);

        when(clienteService.getClienteById(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/cliente/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetClienteById_notFound() throws Exception {
        when(clienteService.getClienteById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/cliente/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testRegistrarCliente() throws Exception {
        RegistroDTO dto = new RegistroDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Pérez");
        dto.setEmail("carlos@mail.com");
        dto.setContrasena("123");
        dto.setTelefono("600123456");
        dto.setMetodoPago("Bizum");

        Cliente guardado = new Cliente("Carlos", "Pérez", "carlos@mail.com", "123", "600123456", "Bizum", "USER");
        guardado.setId(10L);

        when(clienteService.registrarCliente(any())).thenReturn(guardado);

        mockMvc.perform(post("/api/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
               .andExpect(status().isOk());
    }

    @Test
    void testActualizarCliente() throws Exception {
        Cliente nuevo = new Cliente("Laura", "Gómez", "laura@mail.com", "clave", "699", "Efectivo", "USER");
        nuevo.setId(20L);

        when(clienteService.updateCliente(eq(20L), any())).thenReturn(nuevo);

        mockMvc.perform(put("/api/cliente/20")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
               .andExpect(status().isOk());
    }

    @Test
    void testEliminarCliente_ok() throws Exception {
        when(clienteService.deleteCliente(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/cliente/5"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarCliente_notFound() throws Exception {
        when(clienteService.deleteCliente(77L)).thenReturn(false);

        mockMvc.perform(delete("/api/cliente/77"))
               .andExpect(status().isNotFound());
    }
}
