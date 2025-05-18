package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import com.example.restapi.testconfig.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(CompraController.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("controller")  
class CompraControllerWebTest {

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
    void crearCompraDesdeEmail() throws Exception {

        // Datos de prueba
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Medicamento medicamento = new Medicamento();
        medicamento.setId(100L);

        Compra compra = new Compra();
        compra.setId(555L);
        compra.setCliente(cliente);
        compra.setFechaCompra(LocalDate.now());

        when(clienteService.findByEmail("juan@email.com"))
                .thenReturn(Optional.of(cliente));
        when(medicamentoRepository.findAllById(List.of(100L)))
                .thenReturn(List.of(medicamento));
        when(compraService.saveCompra(any(Compra.class)))
                .thenReturn(compra);

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
