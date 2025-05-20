package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CarritoTest {

    @Test
    void testConstructorYGettersBasicos() {
        Cliente cliente = new Cliente();
        Carrito carrito = new Carrito(cliente);

        assertThat(carrito.getCliente()).isEqualTo(cliente);
        assertThat(carrito.getItems()).isEmpty();
        assertThat(carrito.getCreado()).isNotNull();
    }

    @Test
    void testAddItem_nuevoItem() {
        Cliente cliente = new Cliente();
        Carrito carrito = new Carrito(cliente);

        Medicamento med = new Medicamento();
        med.setId(1L);
        med.setPrecio(2.5);

        carrito.addItem(med, 2);

        assertThat(carrito.getItems()).hasSize(1);
        CarritoItem item = carrito.getItems().get(0);
        assertThat(item.getCantidad()).isEqualTo(2);
        assertThat(item.getMedicamento()).isEqualTo(med);
    }

    @Test
    void testAddItem_incrementaCantidad() {
        Cliente cliente = new Cliente();
        Carrito carrito = new Carrito(cliente);

        Medicamento med = new Medicamento();
        med.setId(1L);
        med.setPrecio(1.0);

        carrito.addItem(med, 1);
        carrito.addItem(med, 3);

        assertThat(carrito.getItems()).hasSize(1);
        assertThat(carrito.getItems().get(0).getCantidad()).isEqualTo(4);
    }

    @Test
    void testRemoveItem() {
        Cliente cliente = new Cliente();
        Carrito carrito = new Carrito(cliente);

        Medicamento med1 = new Medicamento();
        med1.setId(1L);

        Medicamento med2 = new Medicamento();
        med2.setId(2L);

        carrito.addItem(med1, 1);
        carrito.addItem(med2, 1);

        carrito.removeItem(1L);

        assertThat(carrito.getItems()).hasSize(1);
        assertThat(carrito.getItems().get(0).getMedicamento().getId()).isEqualTo(2L);
    }

    @Test
    void testGetTotal() {
        Cliente cliente = new Cliente();
        Carrito carrito = new Carrito(cliente);

        Medicamento med1 = new Medicamento();
        med1.setId(1L);
        med1.setPrecio(1.5);

        Medicamento med2 = new Medicamento();
        med2.setId(2L);
        med2.setPrecio(3.0);

        carrito.addItem(med1, 2); // 3.0
        carrito.addItem(med2, 1); // 3.0

        assertThat(carrito.getTotal()).isEqualTo(6.0);
    }

    @Test
    void testSettersAdicionales() {
        Carrito carrito = new Carrito();
        carrito.setCreado(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertThat(carrito.getCreado().getYear()).isEqualTo(2023);
    }

    @Test
    void testSettersYGettersFaltantes() {
        Carrito carrito = new Carrito();
        carrito.setCliente(new Cliente());
        carrito.setItems(List.of()); // set explícito

        assertThat(carrito.getCliente()).isNotNull();
        assertThat(carrito.getItems()).isEmpty();
        assertThat(carrito.getId()).isNull(); // todavía no persistido, será null
    }

    @Test
    void testGetTotalConListaVacia() {
        Carrito carrito = new Carrito();
        assertThat(carrito.getTotal()).isEqualTo(0.0);
    }

}
