package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    @InjectMocks
    private CompraService service;

    @Mock
    private CompraRepository compraRepo;

    private Cliente ana;
    private Medicamento ibup;
    private Compra compraPendiente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ana = new Cliente("Ana", "López", "ana@demo.es", "HASH", "600", "Tarjeta");
        ibup = new Medicamento("Ibuprofeno", "Analgésico", 5, 30, "Bayer");
        compraPendiente = new Compra(
                ana, List.of(ibup), LocalDate.now(), 2, ana.getMetodoPago());
    }

    @Test
    void historialPorCliente_mapeaADTO() {
        when(compraRepo.findByClienteId(1L)).thenReturn(List.of(compraPendiente));

        List<CompraResumenDTO> res = service.getHistorialPorCliente(1L);

        assertThat(res).singleElement()
                       .extracting(CompraResumenDTO::getEstado)
                       .isEqualTo("Pendiente");
    }

    @Test
    void estadoCompraDTO_devuelveDatosDetallados() {
        when(compraRepo.findById(99L)).thenReturn(Optional.of(compraPendiente));

        assertThat(service.getEstadoCompraDTO(99L))
                .isPresent()
                .get()
                .extracting(dto -> dto.getCliente().getEmail()) // <-- Corregido: getEmail()
                .isEqualTo("ana@demo.es");
    }
}
