package com.example.restapi.repository;

import com.example.restapi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest                   // solo capa JPA
@ActiveProfiles("test")        // usa application-test.properties (H2)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CompraRepositoryIT {

    @Autowired private CompraRepository  compraRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private MedicamentoRepository medRepo;

    private Cliente juan;
    private Cliente ana;

    @BeforeEach
    void setUp() {
        // Clientes
        juan = clienteRepo.save(new Cliente(
                "Juan","Pérez","juan@demo.es","1234","600111111","Tarjeta"));
        ana = clienteRepo.save(new Cliente(
                "Ana","López","ana@demo.es","1234","600222222","Tarjeta"));

        // Medicamentos
        Medicamento ibup = medRepo.save(new Medicamento(
                "Ibuprofeno","Analgesico",5.0,100,"Bayer"));
        Medicamento para = medRepo.save(new Medicamento(
                "Paracetamol","Analgesico",3.0,100,"Bayer"));

        // Compras de Juan
        compraRepo.save(new Compra(juan, List.of(ibup),
                LocalDate.now(), 2, juan.getMetodoPago()));            // estado Pendiente
        Compra enviada = new Compra(juan, List.of(para),
                LocalDate.now(), 1, juan.getMetodoPago());
        enviada.confirmarCompra();                                    // estado Enviado
        compraRepo.save(enviada);

        // Compra de Ana
        Compra entregada = new Compra(ana, List.of(ibup, para),
                LocalDate.now(), 3, ana.getMetodoPago());
        entregada.entregarCompra();                                   // estado Entregado
        compraRepo.save(entregada);
    }

    @Test
    void findByClienteId_devuelveSoloComprasDelCliente() {
        List<Compra> comprasJuan = compraRepo.findByClienteId(juan.getId());
        List<Compra> comprasAna  = compraRepo.findByClienteId(ana.getId());

        assertThat(comprasJuan).hasSize(2);
        assertThat(comprasAna).hasSize(1);
    }

    @Test
    void findByEstado_devuelveComprasConEseEstado() {
        List<Compra> pendientes = compraRepo.findByEstado("Pendiente");
        List<Compra> enviados   = compraRepo.findByEstado("Enviado");
        List<Compra> entregados = compraRepo.findByEstado("Entregado");

        assertThat(pendientes).hasSize(1);
        assertThat(enviados).hasSize(1);
        assertThat(entregados).hasSize(1);
    }
}
