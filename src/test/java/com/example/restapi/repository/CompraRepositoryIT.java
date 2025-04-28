package com.example.restapi.repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CompraRepositoryIT {

    @Autowired
    private CompraRepository compraRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private MedicamentoRepository medicamentoRepo;

    private Cliente cliente;
    private Medicamento medicamento;

    @BeforeEach
    void setUp() {
        cliente = clienteRepo.save(new Cliente(
                "Ana", "López", "ana@demo.es", "HASH", "600", "Tarjeta", "USER"));

        medicamento = medicamentoRepo.save(new Medicamento(
                "Ibuprofeno", "Analgésico", 5, 30, "Bayer"));

        Compra compra = new Compra(
                cliente,
                List.of(medicamento),
                LocalDate.now(),
                2,
                cliente.getMetodoPago()
        );
        compraRepo.save(compra);
    }

    @Test
    void findByClienteId_devuelveComprasDelCliente() {
        List<Compra> compras = compraRepo.findByClienteId(cliente.getId());
        assertThat(compras).isNotEmpty();
        assertThat(compras.get(0).getCliente().getEmail()).isEqualTo(cliente.getEmail());
    }

    @Test
    void findByEstado_devuelveComprasPorEstado() {
        List<Compra> pendientes = compraRepo.findByEstado("Pendiente");
        assertThat(pendientes).isNotEmpty();
        assertThat(pendientes.get(0).getEstado()).isEqualTo("Pendiente");
    }

    @Test
    void findByEstado_noDevuelveNadaSiEstadoNoCoincide() {
        List<Compra> entregadas = compraRepo.findByEstado("Entregado");
        assertThat(entregadas).isEmpty();
    }
}
