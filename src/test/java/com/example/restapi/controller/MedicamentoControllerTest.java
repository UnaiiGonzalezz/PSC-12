package com.example.restapi.controller;
import org.springframework.context.annotation.Import;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import com.example.restapi.service.MedicamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(MedicamentoController.class)
class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @Test
    void testGetAllMedicamentos() throws Exception {
        when(medicamentoService.getAllMedicamentos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/medicamentos"))
                .andExpect(status().isOk());
    }
}
