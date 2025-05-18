package com.example.restapi;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.CompraRepository;
import com.example.restapi.repository.MedicamentoRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class DataLoaderTest {
    @Tag(name = "unit")
    @Test
    void testInitDatabase_runsWithoutErrors() throws Exception {
        // Mocks
        ClienteRepository clienteRepo = mock(ClienteRepository.class);
        MedicamentoRepository medicamentoRepo = mock(MedicamentoRepository.class);
        CompraRepository compraRepo = mock(CompraRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);

        // Medicamento de prueba para el .get(0) de la compra
        Medicamento mockParacetamol = new Medicamento("Paracetamol", "Antiinflamatorio", 1.0, 100, "X");
        Medicamento mockIbuprofeno = new Medicamento("Ibuprofeno", "Antiinflamatorio", 1.0, 100, "X");

        // Clientes: simular que no existen
        when(clienteRepo.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clienteRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Medicamentos para la carga inicial: simular que NO existen
        when(medicamentoRepo.findByNombreIgnoreCase(argThat(n -> !n.equalsIgnoreCase("Paracetamol") && !n.equalsIgnoreCase("Ibuprofeno"))))
                .thenReturn(Collections.emptyList());

        // Medicamentos para la compra: simular que SÍ existen
        when(medicamentoRepo.findByNombreIgnoreCase("Paracetamol")).thenReturn(List.of(mockParacetamol));
        when(medicamentoRepo.findByNombreIgnoreCase("Ibuprofeno")).thenReturn(List.of(mockIbuprofeno));

        // Guardar medicamentos
        when(medicamentoRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Compra
        when(compraRepo.count()).thenReturn(0L);
        when(compraRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Codificador
        when(encoder.encode(anyString())).thenReturn("encodedPass");

        // Ejecutar DataLoader
        DataLoader loader = new DataLoader();
        CommandLineRunner runner = loader.initDatabase(clienteRepo, medicamentoRepo, compraRepo, encoder);

        assertNotNull(runner);
        runner.run();

        // Verificar que al menos 1 medicamento fue guardado
        verify(medicamentoRepo, atLeastOnce()).save(any(Medicamento.class));
        verify(compraRepo).save(any(Compra.class));
        verify(clienteRepo, atLeastOnce()).save(any(Cliente.class));
    }
}
