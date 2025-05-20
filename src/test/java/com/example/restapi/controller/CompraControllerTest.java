package com.example.restapi.controller;

import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.EstadoCompraDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(CompraController.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("controller")
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarCompras() throws Exception {
        when(compraService.getAllCompras()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/compras"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetEstadoCompra_found() throws Exception {
        EstadoCompraDTO dto = new EstadoCompraDTO();
        when(compraService.getEstadoCompraDTO(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/compras/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetEstadoCompra_notFound() throws Exception {
        when(compraService.getEstadoCompraDTO(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/compras/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCambiarEstado_ok() throws Exception {
        Compra compra = new Compra();
        compra.setEstado("Entregado");

        when(compraService.updateEstado(eq(1L), eq("Entregado"))).thenReturn(compra);

        mockMvc.perform(patch("/compras/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"Entregado\"}"))
               .andExpect(status().isOk());
    }

    @Test
    void testCambiarEstado_estadoVacio() throws Exception {
        mockMvc.perform(patch("/compras/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"\"}"))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testCompraRequestSettersAndGetters() {
        CompraController.CompraRequest request = new CompraController.CompraRequest();

        request.setEmail("test@mail.com");
        request.setCantidad(2);
        request.setMetodoPago("Tarjeta");
        request.setMedicamentoIds(List.of(1L, 2L));

        assertThat(request.getEmail()).isEqualTo("test@mail.com");
        assertThat(request.getCantidad()).isEqualTo(2);
        assertThat(request.getMetodoPago()).isEqualTo("Tarjeta");
        assertThat(request.getMedicamentoIds()).containsExactly(1L, 2L);
    }

    @Test
    void testCompraRequestConstructorVacioYSetters() {
        var req = new CompraController.CompraRequest();
        req.setEmail(null);
        req.setCantidad(0);
        req.setMetodoPago(null);
        req.setMedicamentoIds(Collections.emptyList());

        assertThat(req.getEmail()).isNull();
        assertThat(req.getCantidad()).isZero();
        assertThat(req.getMetodoPago()).isNull();
        assertThat(req.getMedicamentoIds()).isEmpty();
    }

    @Test
    void testConvertToDTO_coversAllFields() throws Exception {
        Compra compra = new Compra();
        compra.setId(123L);

        var cliente = new com.example.restapi.model.Cliente();
        cliente.setId(1L);
        cliente.setNombre("John");
        cliente.setApellido("Doe");
        compra.setCliente(cliente);

        var medicamento = new com.example.restapi.model.Medicamento();
        medicamento.setId(10L);
        medicamento.setNombre("Paracetamol");
        medicamento.setCategoria("Analgésico");
        medicamento.setPrecio(5.99);
        medicamento.setStock(100);
        medicamento.setProveedor("ProveedorX");
        medicamento.setDisponible(true);

        compra.setMedicamentos(List.of(medicamento));
        compra.setCantidad(3);
        compra.setMetodoPago("Tarjeta");
        compra.setEstado("Pendiente");
        compra.setPago(17.97);
        compra.setFechaCompra(java.time.LocalDate.now());

        when(compraService.getAllCompras()).thenReturn(List.of(compra));

        mockMvc.perform(get("/compras"))
            .andExpect(status().isOk());
    }





}
