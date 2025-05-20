package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void testConstructorManualYGetters() {
        Cliente cliente = new Cliente(
                "Luis", "Martín", "luis@mail.com", "original", "666123456", "Efectivo", "USER"
        );

        cliente.setId(10L); // cubrir setter lombok
        cliente.setPassword("alias"); // cubrir alias setter

        assertThat(cliente.getId()).isEqualTo(10L);
        assertThat(cliente.getNombre()).isEqualTo("Luis");
        assertThat(cliente.getApellido()).isEqualTo("Martín");
        assertThat(cliente.getEmail()).isEqualTo("luis@mail.com");
        assertThat(cliente.getContrasena()).isEqualTo("alias"); // alias sobrescribe
        assertThat(cliente.getPassword()).isEqualTo("alias");   // alias getter
        assertThat(cliente.getTelefono()).isEqualTo("666123456");
        assertThat(cliente.getMetodoPago()).isEqualTo("Efectivo");
        assertThat(cliente.getRol()).isEqualTo("USER");
    }

    @Test
    void testNoArgsConstructorSettersYAlias() {
        Cliente cliente = new Cliente();

        cliente.setId(77L);
        cliente.setNombre("Ana");
        cliente.setApellido("Ramos");
        cliente.setEmail("ana@mail.com");
        cliente.setContrasena("clave123");
        cliente.setPassword("finalClave");
        cliente.setTelefono("644000000");
        cliente.setMetodoPago("Bizum");
        cliente.setRol("ADMIN");

        // alias test again
        assertThat(cliente.getPassword()).isEqualTo("finalClave");
        assertThat(cliente.getContrasena()).isEqualTo("finalClave");
    }

    @Test
    void testLombokConstructoresYToquesMinimos() {
        // Constructor sin args
        Cliente clienteVacio = new Cliente();
        assertThat(clienteVacio).isNotNull();

        // Constructor completo (AllArgsConstructor de Lombok)
        Cliente clienteFull = new Cliente(
                100L, "Mario", "Suárez", "mario@mail.com",
                "lombokPass", "700000000", "PayPal", "ADMIN"
        );

        // Cobertura de todos los getters automáticos
        assertThat(clienteFull.getId()).isEqualTo(100L);
        assertThat(clienteFull.getNombre()).isEqualTo("Mario");
        assertThat(clienteFull.getApellido()).isEqualTo("Suárez");
        assertThat(clienteFull.getEmail()).isEqualTo("mario@mail.com");
        assertThat(clienteFull.getContrasena()).isEqualTo("lombokPass");
        assertThat(clienteFull.getTelefono()).isEqualTo("700000000");
        assertThat(clienteFull.getMetodoPago()).isEqualTo("PayPal");
        assertThat(clienteFull.getRol()).isEqualTo("ADMIN");
    }
}
