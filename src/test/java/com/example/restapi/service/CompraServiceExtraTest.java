package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CompraServiceExtraTest {

    @InjectMocks private CompraService compraService;

    @Mock private CompraRepository compraRepo;

    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("Eva", "Martín", "eva@demo.es", "HASH", "600123456", "Tarjeta", "USER");
        medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 50, "Cinfa");
    }

    @Test
    void historialCliente_vacio() {
        when(compraRepo.findByClienteId(99L)).thenReturn(Collections.emptyList());

        List<CompraResumenDTO> result = compraService.getHistorialPorCliente(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void crearDesdeCarrito_ok() {
        Carrito carrito = new Carrito(cliente);
        carrito.addItem(medicamento, 1);

        when(compraRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Compra result = compraService.crearDesdeCarrito(carrito);

        assertThat(result.getCliente().getEmail()).isEqualTo("eva@demo.es");
    }
}
