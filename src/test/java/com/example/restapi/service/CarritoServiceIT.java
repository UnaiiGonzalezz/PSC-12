package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.repository.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Tag("service")
class CarritoServiceIT {

    @Autowired private MedicamentoRepository medRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private CarritoService carritoService;
    @Autowired private CompraRepository compraRepo;

    @Test
    void checkoutFlujoCompleto() {
        // Arrange
        Medicamento ibuprofeno = new Medicamento("Ibuprofeno", "Analgesico", 5.0, 100, "Bayer");
        ibuprofeno = medRepo.save(ibuprofeno);

        Cliente ana = new Cliente("Ana", "Lopez", "ana@demo.es", "1234", "600000000", "Tarjeta", "USER");
        ana = clienteRepo.save(ana);

        // Act
        carritoService.addMedicamento(ana, ibuprofeno.getId(), 2);
        var response = carritoService.checkout(ana);

        // Assert
        assertThat(response.getTotal()).isEqualTo(10.0);
        assertThat(compraRepo.findById(response.getCompraId())).isPresent();
        assertThat(medRepo.findById(ibuprofeno.getId()).get().getStock()).isEqualTo(98);
    }
}
