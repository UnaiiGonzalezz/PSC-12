package com.example.restapi.controller;

import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.service.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
        // mock updateEstado(...)
        Compra compra = new Compra();
        compra.setId(5L);
        compra.setEstado("Enviado");
        Mockito.when(compraService.updateEstado(anyLong(), anyString()))
               .thenReturn(compra);

        // mock getEstadoCompraDTO(...)
        EstadoCompraDTO dto = new EstadoCompraDTO(
                5L, "Enviado", LocalDate.now(), null, null);
        Mockito.when(compraService.getEstadoCompraDTO(5L))
               .thenReturn(Optional.of(dto));
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
