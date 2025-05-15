package com.example.restapi.controller;
import org.springframework.context.annotation.Import;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import com.example.restapi.model.dto.CompraDTO;
import com.example.restapi.service.CompraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import static org.mockito.ArgumentMatchers.anyLong;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(CarritoController.class)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @Test
    void testListarCompras() throws Exception {
        when(compraService.getHistorialPorCliente(anyLong())).thenReturn(Collections.emptyList());


        mockMvc.perform(get("/api/compras"))
                .andExpect(status().isOk());
    }
}
