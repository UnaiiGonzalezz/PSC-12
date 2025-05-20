package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.MedicamentoService;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@WebMvcTest(MedicamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("controller")
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

    @Test
    void testGetMedicamentoById_found() throws Exception {
        Medicamento med = new Medicamento("Ibuprofeno", "Analgésico", 3.99, 50, "ProveedorX");
        med.setId(1L);
        when(medicamentoService.getMedicamentoById(1L)).thenReturn(Optional.of(med));

        mockMvc.perform(get("/medicamentos/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetMedicamentoById_notFound() throws Exception {
        when(medicamentoService.getMedicamentoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/medicamentos/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testGetMedicamentosPorNombre() throws Exception {
        when(medicamentoService.getMedicamentosPorNombre("para")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicamentos/nombre/para"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetMedicamentosPorCategoria() throws Exception {
        when(medicamentoService.getMedicamentosPorCategoria("antibiotico")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicamentos/categoria/antibiotico"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetMedicamentosDisponibles() throws Exception {
        when(medicamentoService.getMedicamentosDisponibles()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicamentos/disponibles"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetMovimientos() throws Exception {
        when(medicamentoService.getMovimientosDeMedicamento(1L)).thenReturn(List.of(new StockMovimiento()));

        mockMvc.perform(get("/medicamentos/1/movimientos"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetMedicamentosPaginados() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(medicamentoService.getMedicamentos(pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/medicamentos/pag?page=0&size=10"))
            .andExpect(status().isOk());
    }

}
