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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CompraControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CompraService compraService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtUtil jwtUtil;

    private EstadoCompraDTO estadoCompraDTO;

    @BeforeEach
    void setUp() {
        Compra compra = new Compra();
        compra.setId(5L);
        compra.setEstado("Enviado");

        Mockito.when(compraService.updateEstado(anyLong(), anyString()))
                .thenReturn(compra);

        estadoCompraDTO = new EstadoCompraDTO(
                5L,
                "Enviado",
                LocalDate.of(2025, 4, 28),
                null,
                null
        );

        Mockito.when(compraService.getEstadoCompraDTO(5L))
                .thenReturn(Optional.of(estadoCompraDTO));
    }

    @Test
    @WithMockUser
    void cambiarEstado() throws Exception {
        mvc.perform(patch("/compras/5/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"Enviado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.estado").value("Enviado"));
    }

    @Test
    @WithMockUser
    void obtenerCompraPorId() throws Exception {
        mvc.perform(get("/compras/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.estado").value("Enviado"));
    }
}
