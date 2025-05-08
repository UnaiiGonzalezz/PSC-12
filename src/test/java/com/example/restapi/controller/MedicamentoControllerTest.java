package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.MedicamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MedicamentoController.class)
@AutoConfigureMockMvc
@Import(MedicamentoControllerTest.TestSecurityConfig.class)
class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @org.springframework.boot.test.context.TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll().and().build();
        }

        @Bean
        public JwtUtil jwtUtil() {
            return new JwtUtil();
        }
    }

    @Test
    void testGetMedicamentoById_found() throws Exception {
        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNombre("Ibuprofeno");
        medicamento.setPrecio(5.00);

        when(medicamentoService.getMedicamentoById(1L)).thenReturn(Optional.of(medicamento));

        mockMvc.perform(get("/medicamentos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Ibuprofeno"))
                .andExpect(jsonPath("$.precio").value(5.00));
    }

    @Test
    void testGetMedicamentoById_notFound() throws Exception {
        when(medicamentoService.getMedicamentoById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/medicamentos/2"))
                .andExpect(status().isNotFound());
    }
}
