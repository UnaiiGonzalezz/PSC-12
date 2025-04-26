package com.example.restapi.controller;

import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompraController.class)
class CompraControllerWebTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        Compra compra = new Compra();
        compra.setId(5L);
        compra.setEstado("Enviado");

        when(compraService.updateEstado(anyLong(), anyString())).thenReturn(compra);
        when(compraService.getEstadoCompraDTO(5L))
                .thenReturn(Optional.of(new EstadoCompraDTO(
                        5L, "Enviado", LocalDate.now(), null, null)));
    }

    @Test
    void cambiarEstado() throws Exception {
        mvc.perform(patch("/compras/5/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"Enviado\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.estado").value("Enviado"));
    }
}
