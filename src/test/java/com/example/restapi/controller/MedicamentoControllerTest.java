package com.example.restapi.controller;

import com.example.restapi.service.MedicamentoService;
import com.example.restapi.security.JwtUtil;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicamentoController.class)
@AutoConfigureMockMvc(addFilters = false)  // filtros deshabilitados para no interferir
@Tag("controller")
class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @MockBean  // Esto es crucial para que JwtUtil esté disponible y no falle el contexto
    private JwtUtil jwtUtil;

    @Test
    void testGetAllMedicamentos() throws Exception {
        when(medicamentoService.getAllMedicamentos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicamentos"))
               .andExpect(status().isOk());
    }
}
