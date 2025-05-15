package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;
    @MockBean
    private JwtUtil jwtUtil;

    /* ---------- helper ---------- */
    private Cliente dummy() {
        Cliente c = new Cliente();
        c.setId(1L);
        c.setNombre("Ana");
        c.setEmail("ana@mail.com");
        return c;
    }

    /* ---------- LISTAR ---------- */
    @Test
    void listarClientes_ok() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(List.of(dummy()));

        mockMvc.perform(get("/api/cliente"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    /* ---------- OBTENER ---------- */
    @Nested
    class GetById {
        @Test
        void obtenerCliente_existente_ok() throws Exception {
            when(clienteService.getClienteById(1L)).thenReturn(dummy());

            mockMvc.perform(get("/api/cliente/1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.email").value("ana@mail.com"));
        }
    }

    /* ---------- REGISTRO ---------- */
    @Test
    void registrarCliente_ok() throws Exception {
        when(clienteService.registrarCliente(any(RegistroDTO.class))).thenReturn(dummy());

        mockMvc.perform(post("/api/cliente")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                           {
                             "nombre":"Ana",
                             "apellido":"Ruiz",
                             "email":"ana@mail.com",
                             "contrasena":"1234"
                           }
                           """))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Ana"));
    }
}
