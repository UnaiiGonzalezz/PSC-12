package com.example.restapi.service;

import com.example.restapi.model.Medicamento;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        verify(repo).save(medicamento);
        verify(movRepo).save(any());
    }

    @Test
    void testGetMedicamentoById_existente() {
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));

        Optional<Medicamento> result = service.getMedicamentoById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Paracetamol");
    }

    @Test
    void testGetMedicamentoById_noExiste() {
        when(repo.findById(2L)).thenReturn(Optional.empty());

        Optional<Medicamento> result = service.getMedicamentoById(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void testGetMedicamentosPorCategoria() {
        when(repo.findByCategoriaIgnoreCase("Analgésico")).thenReturn(List.of(medicamento));

        List<Medicamento> result = service.getMedicamentosPorCategoria("Analgésico");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Paracetamol");
    }

    @Test
    void testUpdateMedicamento_existente() {
        Medicamento updatedMedicamento = new Medicamento("Paracetamol", "Analgésico", 4.0, 120, "Cinfa");
        updatedMedicamento.setId(1L);

        // Mock para devolver el medicamento original
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));

        // Mock para guardar el medicamento actualizado
        when(repo.save(any(Medicamento.class))).thenReturn(updatedMedicamento);

        // Llamada al método de servicio
        Medicamento result = service.updateMedicamento(1L, updatedMedicamento);

        // Asegurarse de que el medicamento devuelto sea el actualizado
        assertThat(result).isNotNull();
        assertThat(result.getPrecio()).isEqualTo(4.0); // Verificar el precio actualizado
        assertThat(result.getStock()).isEqualTo(120);  // Verificar el stock actualizado
        assertThat(result.getNombre()).isEqualTo("Paracetamol"); // Verificar el nombre
    }

    @Test
    void testDeleteMedicamento() {
        when(repo.findById(1L)).thenReturn(Optional.of(medicamento));

        service.deleteMedicamento(1L);

        verify(repo).delete(medicamento);
        verify(movRepo).deleteByMedicamento(medicamento);
    }
}
