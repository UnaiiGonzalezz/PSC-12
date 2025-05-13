package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.service.MedicamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicamentoController.class)  // Usa WebMvcTest para cargar solo el controlador
public class MedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;

    @Test
    void testCreateMedicamento() throws Exception {
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Aspirina");
        medicamento.setCategoria("Analgésico");
        medicamento.setPrecio(2.5);
        medicamento.setStock(100);
        medicamento.setProveedor("Bayer");

        when(medicamentoService.saveMedicamento(medicamento)).thenReturn(medicamento);

        mockMvc.perform(post("/medicamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Aspirina\", \"categoria\": \"Analgésico\", \"precio\": 2.5, \"stock\": 100, \"proveedor\": \"Bayer\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Aspirina"))
                .andExpect(jsonPath("$.precio").value(2.5))
                .andExpect(jsonPath("$.categoria").value("Analgésico"));
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
