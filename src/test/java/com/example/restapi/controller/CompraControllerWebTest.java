package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import com.example.restapi.testconfig.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
@Import(TestSecurityConfig.class)
public class CompraControllerWebTest {

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

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void obtenerCompraPorId() throws Exception {
        EstadoCompraDTO estadoCompraDTO = new EstadoCompraDTO();
        estadoCompraDTO.setId(99L);
        estadoCompraDTO.setClienteId(1L);
        estadoCompraDTO.setEstado("Pendiente");

        when(compraService.getEstadoCompraDTO(99L)).thenReturn(Optional.of(estadoCompraDTO));

        mockMvc.perform(get("/compras/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.estado").value("Pendiente"));
    }

    @Test
    void cambiarEstado() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Laura");

        Medicamento med = new Medicamento("Ibuprofeno", "Analgésico", 10.0, 20, "Bayer");
        med.setId(15L);

        Compra compra = new Compra(cliente, List.of(med), LocalDate.now(), 1, "Transferencia");
        compra.setId(100L);
        compra.setEstado("Completado");

        when(compraService.updateEstado(100L, "Completado")).thenReturn(compra);

        Map<String, String> estadoMap = Map.of("estado", "Completado");

        mockMvc.perform(patch("/compras/100/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(estadoMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    @Test
    void crearCompraDesdeEmail() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");

        Medicamento medicamento = new Medicamento("Ibuprofeno", "Analgésico", 10.0, 50, "Bayer");
        medicamento.setId(100L);

        Compra compra = new Compra(cliente, List.of(medicamento), LocalDate.now(), 2, "Efectivo");
        compra.setId(555L);
        compra.setPago(20.0);

        when(clienteService.findByEmail("juan@email.com")).thenReturn(Optional.of(cliente));
        when(medicamentoRepository.findAllById(List.of(100L))).thenReturn(List.of(medicamento));
        when(compraService.saveCompra(any(Compra.class))).thenReturn(compra);

        Map<String, Object> body = Map.of(
                "email", "juan@email.com",
                "medicamentoIds", List.of(100),
                "cantidad", 2,
                "metodoPago", "Efectivo"
        );

        mockMvc.perform(post("/compras/crear-por-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(555));
    }
}
