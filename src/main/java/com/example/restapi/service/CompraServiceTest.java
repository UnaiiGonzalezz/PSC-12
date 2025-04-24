package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.model.stock.MovimientoTipo;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    @InjectMocks
    private CompraService service;

    @Mock private CompraRepository compraRepo;

    private Cliente     ana;
    private Medicamento ibup;
    private Compra      compraPendiente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ana  = new Cliente("Ana","López","ana@demo.es","HASH","600","Tarjeta");
        ibup = new Medicamento("Ibuprofeno","Analgesico",5,30,"Bayer");

        compraPendiente = new Compra(ana,
                List.of(ibup), LocalDate.now(), 2, ana.getMetodoPago());
    }

    /* ---------- getHistorialPorCliente ---------- */
    @Test
    void historialPorCliente_mapeaADTO() {
        when(compraRepo.findByClienteId(1L)).thenReturn(List.of(compraPendiente));

        List<CompraResumenDTO> res = service.getHistorialPorCliente(1L);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getEstado()).isEqualTo("Pendiente");
    }

    /* ---------- getEstadoCompraDTO ---------- */
    @Test
    void estadoCompraDTO_devuelveDatosDetallados() {
        when(compraRepo.findById(99L)).thenReturn(Optional.of(compraPendiente));

        assertThat(service.getEstadoCompraDTO(99L)).isPresent()
                .get().extracting("cliente.email")
                .isEqualTo("ana@demo.es");
    }

    /* Si añadiste método updateEstado(...) en el servicio,
       aquí iría un test similar con verify(compraRepo).save(...)                     */
}
