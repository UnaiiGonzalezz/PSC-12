package com.example.restapi.controller;

import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.MedicamentoService;
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
@WebMvcTest(MedicamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllMedicamentos() throws Exception {
        when(medicamentoService.getAllMedicamentos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicamentos"))
               .andExpect(status().isOk());
    }
}
