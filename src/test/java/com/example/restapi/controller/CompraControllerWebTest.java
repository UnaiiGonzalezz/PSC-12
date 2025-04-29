package com.example.restapi.controller;

import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
class CompraControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CompraService compraService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    private Compra compra;

    @BeforeEach
    void setUp() {
        compra = new Compra();
        compra.setId(5L);
        compra.setEstado("Enviado");

        // Mock PATCH actualización de estado
        Mockito.when(compraService.updateEstado(anyLong(), anyString()))
                .thenReturn(compra);

        // Mock GET estado detallado
        EstadoCompraDTO dto = new EstadoCompraDTO(
                5L, "Enviado", LocalDate.now(), null, null);
        Mockito.when(compraService.getEstadoCompraDTO(5L))
                .thenReturn(Optional.of(dto));

        // Mock GET compra por ID
        Mockito.when(compraService.getCompraById(5L))
                .thenReturn(Optional.of(compra));
    }

    @Test
    @WithMockUser
    void cambiarEstado() throws Exception {
        mvc.perform(patch("/compras/5/estado")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"Enviado\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("Enviado"));
    }

    @Test
    @WithMockUser
    void obtenerCompraPorId() throws Exception {
        mvc.perform(get("/compras/5")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(5L))
            .andExpect(jsonPath("$.estado").value("Enviado"));
    }
}
