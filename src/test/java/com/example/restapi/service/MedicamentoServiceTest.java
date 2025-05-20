package com.example.restapi.service;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Tag("service")
class MedicamentoServiceTest {

    @InjectMocks
    private MedicamentoService service;

    @Mock
    private MedicamentoRepository repo;

    @Mock
    private StockMovimientoRepository movRepo;

    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicamento = new Medicamento("Paracetamol", "Analgésico", 3.0, 100, "Cinfa");
        medicamento.setId(1L);
    }

    @Test
    void testSaveMedicamento() {
        when(repo.save(medicamento)).thenReturn(medicamento);
        Medicamento result = service.saveMedicamento(medicamento);
        assertThat(result).isEqualTo(medicamento);
        verify(movRepo).save(any());
    }

    @Test
    void testGetMedicamentoById_existente() {
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        assertThat(service.getMedicamentoById(1L)).isPresent();
    }

    @Test
    void testGetMedicamentoById_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.getMedicamentoById(99L)).isEmpty();
    }

    @Test
    void testGetMedicamentosPorCategoria() {
        when(repo.findByCategoriaIgnoreCase("Analgésico")).thenReturn(List.of(medicamento));
        assertThat(service.getMedicamentosPorCategoria("Analgésico")).hasSize(1);
    }

    @Test
    void testUpdateMedicamento_existente_conStock() {
        Medicamento nuevo = new Medicamento("Ibuprofeno", "Anti-inflamatorio", 5.0, 20, "Bayer");
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        when(repo.save(any())).thenReturn(nuevo);
        Medicamento actualizado = service.updateMedicamento(1L, nuevo);
        assertThat(actualizado.getNombre()).isEqualTo("Ibuprofeno");
    }

    @Test
    void testUpdateMedicamento_existente_sinStock() {
        Medicamento nuevo = new Medicamento("Ibuprofeno", "Anti-inflamatorio", 5.0, 0, "Bayer");
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        when(repo.save(any())).thenReturn(nuevo);
        Medicamento actualizado = service.updateMedicamento(1L, nuevo);
        assertThat(actualizado.getStock()).isEqualTo(0);
    }

    @Test
    void testUpdateMedicamento_noExiste() {
        Medicamento nuevo = new Medicamento("Ibuprofeno", "Anti-inflamatorio", 5.0, 20, "Bayer");
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateMedicamento(2L, nuevo));
    }

    @Test
    void testDeleteMedicamento() {
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        service.deleteMedicamento(1L);
        verify(repo).delete(medicamento);
        verify(movRepo).deleteByMedicamento(medicamento);
    }

    @Test
    void testDeleteMedicamento_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.deleteMedicamento(99L));
    }

    @Test
    void testGetAllMedicamentos() {
        when(repo.findAll()).thenReturn(List.of(medicamento, medicamento));
        assertThat(service.getAllMedicamentos()).hasSize(2);
    }

    @Test
    void testGetMedicamentosDisponibles() {
        medicamento.setDisponible(true);
        when(repo.findByDisponibleTrue()).thenReturn(List.of(medicamento));
        assertThat(service.getMedicamentosDisponibles()).hasSize(1);
    }


    @Test
    void testGetMedicamentosPorNombre() {
        when(repo.findByNombreIgnoreCase("Paracetamol")).thenReturn(List.of(medicamento));
        assertThat(service.getMedicamentosPorNombre("Paracetamol")).hasSize(1);
    }

    @Test
    void testGetMovimientosDeMedicamento_existente() {
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        when(movRepo.findByMedicamentoOrderByFechaDesc(medicamento)).thenReturn(List.of(new StockMovimiento()));
        assertThat(service.getMovimientosDeMedicamento(1L)).hasSize(1);
    }

    @Test
    void testGetMovimientosDeMedicamento_noExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getMovimientosDeMedicamento(99L));
    }

    @Test
    void testUpdateMedicamento_mismoStock_noGuardaMovimiento() {
        Medicamento nuevo = new Medicamento("Ibuprofeno", "Anti-inflamatorio", 5.0, 100, "Bayer"); // mismo stock = 100
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));
        when(repo.save(any())).thenReturn(nuevo);
        service.updateMedicamento(1L, nuevo);
        verify(movRepo, never()).save(any()); // no guarda movimiento porque no cambia el stock
    }

    @Test
    void testGetMedicamentosConPaginacion() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Medicamento> page = new PageImpl<>(List.of(medicamento));
        when(repo.findAll(pageable)).thenReturn(page);
        Page<MedicamentoDTO> result = service.getMedicamentos(pageable);
        assertThat(result.getContent()).hasSize(1);
    }
}
