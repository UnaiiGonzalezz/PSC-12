package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CarritoServiceIT {

    @Autowired private MedicamentoRepository medRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private CarritoService carritoService;
    @Autowired private CompraRepository compraRepo;

    @Test
    void checkoutFlujoCompleto() {
        Medicamento ibup = medRepo.save(new Medicamento("Ibuprofeno", "Analgesico", 5.0, 100, "Bayer"));
        Cliente ana = clienteRepo.save(new Cliente("Ana", "Lopez", "ana@demo.es", "1234", "600000000", "Tarjeta", "USER"));

        carritoService.addMedicamento(ana, ibup.getId(), 2);
        var resp = carritoService.checkout(ana);

        assertThat(resp.getTotal()).isEqualTo(10.0);
        assertThat(compraRepo.findById(resp.getCompraId())).isPresent();
        assertThat(medRepo.findById(ibup.getId()).get().getStock()).isEqualTo(98);
    }
}
