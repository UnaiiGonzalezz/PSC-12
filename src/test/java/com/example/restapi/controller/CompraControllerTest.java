package com.example.restapi.controller;

import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(CompraController.class)
@AutoConfigureMockMvc(addFilters = false)
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private MedicamentoRepository medicamentoRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testListarCompras() throws Exception {
        when(compraService.getAllCompras()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/compras"))
               .andExpect(status().isOk());
    }
}
