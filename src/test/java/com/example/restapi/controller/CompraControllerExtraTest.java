/*package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.service.CompraService;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)   // desactiva filtros de Spring-Security
@Tag("controller")
class CompraControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    private Compra dummyCompra() {
        Cliente cli = new Cliente();
        cli.setId(1L);
        cli.setNombre("Ana");

        Compra c = new Compra();
        c.setId(1L);
        c.setCliente(cli);
        return c;
    }

    @Test
    void getAllCompras_ok() throws Exception {
        when(compraService.getAllCompras()).thenReturn(List.of(dummyCompra()));

        mockMvc.perform(get("/compras"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)));
    }

    @Nested
    class Estado {

        @Test
        void getEstadoCompra_notFound() throws Exception {
            when(compraService.getCompraById(anyLong())).thenReturn(Optional.empty());

            mockMvc.perform(get("/compras/estado/99"))
                   .andExpect(status().isNotFound());
        }
    }
}
*/