package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CarritoItemTest {

    @Test
    void testConstructorYGetters() {
        Carrito carrito = new Carrito();
        Medicamento medicamento = new Medicamento("Ibuprofeno", "Antiinflamatorio", 2.0, 50, "Bayer");
        medicamento.setId(1L);

        CarritoItem item = new CarritoItem(carrito, medicamento, 3);

        assertThat(item.getCarrito()).isEqualTo(carrito);
        assertThat(item.getMedicamento()).isEqualTo(medicamento);
        assertThat(item.getCantidad()).isEqualTo(3);
        assertThat(item.getSubtotal()).isEqualTo(6.0);
    }

    @Test
    void testSettersIndividualesYSubtotalNulo() {
        CarritoItem item = new CarritoItem();
        item.setCantidad(5);
        item.setCarrito(new Carrito());

        assertThat(item.getCantidad()).isEqualTo(5);
        assertThat(item.getCarrito()).isNotNull();

        // Test de seguridad: medicamento nulo = subtotal 0.0
        item.setMedicamento(null);
        assertThat(item.getSubtotal()).isEqualTo(0.0);
    }

    @Test
    void testSetYGetId() {
        CarritoItem item = new CarritoItem();
        item.setCantidad(1);
        assertThat(item.getId()).isNull(); // todavía no persistido
    }
}
