package com.example.restapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompraTest {

    private Cliente cliente;
    private Medicamento med1;
    private Medicamento med2;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Ana", "García", "ana@mail.com", "HASH", "600000000", "Tarjeta", "USER");

        med1 = new Medicamento("Paracetamol", "Analgésico", 2.5, 10, "Cinfa");
        med2 = new Medicamento("Ibuprofeno", "Antiinflamatorio", 3.0, 5, "Bayer");
    }

    @Test
    void testConstructorYGetters() {
        Compra compra = new Compra(cliente, List.of(med1, med2), LocalDate.of(2024, 5, 1), 2, "Tarjeta");

        assertThat(compra.getCliente()).isEqualTo(cliente);
        assertThat(compra.getMedicamentos()).containsExactly(med1, med2);
        assertThat(compra.getCantidad()).isEqualTo(2);
        assertThat(compra.getMetodoPago()).isEqualTo("Tarjeta");
        assertThat(compra.getEstado()).isEqualTo("Pendiente");
        assertThat(compra.getPago()).isEqualTo(5.5);
    }

    @Test
    void testSetters() {
        Compra compra = new Compra();
        compra.setId(1L);
        compra.setCliente(cliente);
        compra.setMedicamentos(List.of(med1));
        compra.setFechaCompra(LocalDate.now());
        compra.setCantidad(1);
        compra.setMetodoPago("Bizum");
        compra.setPago(4.2);
        compra.setEstado("Preparado");

        assertThat(compra.getId()).isEqualTo(1L);
        assertThat(compra.getCliente()).isEqualTo(cliente);
        assertThat(compra.getMedicamentos()).containsExactly(med1);
        assertThat(compra.getMetodoPago()).isEqualTo("Bizum");
        assertThat(compra.getPago()).isEqualTo(4.2);
        assertThat(compra.getEstado()).isEqualTo("Preparado");
    }

    @Test
    void testConfirmarEntregarCancelarCompra() {
        Compra compra = new Compra();
        compra.confirmarCompra();
        assertThat(compra.getEstado()).isEqualTo("Enviado");

        compra.entregarCompra();
        assertThat(compra.getEstado()).isEqualTo("Entregado");

        compra.cancelarCompra();
        assertThat(compra.getEstado()).isEqualTo("Cancelada");
    }

    @Test
    void testToStringNoRevienta() {
        Compra compra = new Compra(cliente, List.of(med1), LocalDate.now(), 1, "Efectivo");
        String result = compra.toString();
        assertThat(result).contains("Compra{");
        assertThat(result).contains("cliente=" + cliente.getNombre());
    }
}
