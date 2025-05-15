package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.MedicamentoService;
import com.example.restapi.testconfig.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(MedicamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicamentoControllerExtraTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoService medicamentoService;
    @MockBean
    private JwtUtil jwtUtil;

    /* ---------- helpers ---------- */
    private Medicamento dummyMed() {
        Medicamento m = new Medicamento();
        m.setId(1L);
        m.setNombre("Paracetamol");
        m.setPrecio(4.5);
        m.setDisponible(true);
        return m;
    }

    /* ---------- LISTAR ---------- */
    @Test
    @DisplayName("GET /medicamentos – lista JSON")
    void getAllMedicamentos_ok() throws Exception {
        when(medicamentoService.getAllMedicamentos()).thenReturn(List.of(dummyMed()));

        mockMvc.perform(get("/medicamentos"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].nombre").value("Paracetamol"));
    }

    /* ---------- OBTENER POR ID ---------- */
    @Nested
    class GetById {
        @Test
        void idExiste_devuelve200() throws Exception {
            when(medicamentoService.getMedicamentoById(1L))
                    .thenReturn(Optional.of(dummyMed()));

            mockMvc.perform(get("/medicamentos/1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.nombre").value("Paracetamol"));
        }

        @Test
        void idNoExiste_devuelve404() throws Exception {
            when(medicamentoService.getMedicamentoById(99L))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/medicamentos/99"))
                   .andExpect(status().isNotFound());
        }
    }

    /* ---------- CREAR ---------- */
    @Test
    void crearMedicamento_ok() throws Exception {
        when(medicamentoService.saveMedicamento(any(Medicamento.class)))
                .thenAnswer(inv -> {
                    Medicamento m = inv.getArgument(0);
                    m.setId(10L);
                    return m;
                });

        mockMvc.perform(post("/medicamentos")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                           {
                             "nombre":"Ibuprofeno",
                             "precio":6.0,
                             "categoria":"Analgésico",
                             "stock":50,
                             "proveedor":"Bayer",
                             "disponible":true
                           }
                           """))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(10))
               .andExpect(jsonPath("$.nombre").value("Ibuprofeno"));
    }

    /* ---------- UPDATE ---------- */
    @Test
    void updateMedicamento_ok() throws Exception {
        when(medicamentoService.updateMedicamento(eq(1L), any(Medicamento.class)))
                .thenReturn(dummyMed());

        mockMvc.perform(put("/medicamentos/1")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content("""
                           { "nombre":"Paracetamol Forte", "precio":5.0 }
                           """))
               .andExpect(status().isOk());

        verify(medicamentoService).updateMedicamento(eq(1L), any(Medicamento.class));
    }

    /* ---------- DELETE ---------- */
    @Nested
    class Delete {
        @Test
        void eliminarExitoso() throws Exception {
            doNothing().when(medicamentoService).deleteMedicamento(1L);

            mockMvc.perform(delete("/medicamentos/1"))
                   .andExpect(status().isNoContent());
        }

        @Test
        void eliminarNoEncontrado() throws Exception {
            doThrow(new IllegalArgumentException()).when(medicamentoService).deleteMedicamento(99L);

            mockMvc.perform(delete("/medicamentos/99"))
                   .andExpect(status().isNotFound());
        }
    }
}
