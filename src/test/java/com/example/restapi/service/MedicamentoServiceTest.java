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

    private Medicamento paracetamol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paracetamol = new Medicamento("Paracetamol", "Analgesico", 3.5, 50, "Bayer");
    }

    @Test
    void saveMedicamento() {
        when(repo.save(paracetamol)).thenReturn(paracetamol);
        Medicamento saved = service.saveMedicamento(paracetamol);

        assertThat(saved.getNombre()).isEqualTo("Paracetamol");
        verify(repo).save(paracetamol);
        verify(movRepo).save(any());  // Verifica movimiento de stock
    }

    @Test
    void findById() {
        when(repo.findById(1L)).thenReturn(Optional.of(paracetamol));

        Optional<Medicamento> res = service.getMedicamentoById(1L);

        assertThat(res).isPresent();
    }

    @Test
    void filterByCategoria() {
        when(repo.findByCategoriaIgnoreCase("Analgesico")).thenReturn(List.of(paracetamol));

        List<Medicamento> lista = service.getMedicamentosPorCategoria("Analgesico");

        assertThat(lista).hasSize(1);
    }
}
