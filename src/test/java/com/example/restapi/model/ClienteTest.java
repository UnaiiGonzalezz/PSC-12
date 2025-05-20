package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void testLombokConstructorYSetters() {
        Cliente cliente = new Cliente(
            "Laura", "García", "laura@mail.com", "HASH", "600000000", "Tarjeta", "USER"
        );

        cliente.setId(9L);
        cliente.setPassword("ALIAS");

        assertThat(cliente.getId()).isEqualTo(9L);
        assertThat(cliente.getNombre()).isEqualTo("Laura");
        assertThat(cliente.getApellido()).isEqualTo("García");
        assertThat(cliente.getEmail()).isEqualTo("laura@mail.com");

        // alias
        assertThat(cliente.getContrasena()).isEqualTo("ALIAS");
        assertThat(cliente.getPassword()).isEqualTo("ALIAS");

        assertThat(cliente.getTelefono()).isEqualTo("600000000");
        assertThat(cliente.getMetodoPago()).isEqualTo("Tarjeta");
        assertThat(cliente.getRol()).isEqualTo("USER");
    }

    @Test
    void testEmptyConstructorYTodoManual() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Carlos");
        cliente.setApellido("Test");
        cliente.setEmail("test@mail.com");
        cliente.setContrasena("123");
        cliente.setTelefono("600");
        cliente.setMetodoPago("Bizum");
        cliente.setRol("ADMIN");

        // Validaciones básicas
        assertThat(cliente.getId()).isEqualTo(1L);
        assertThat(cliente.getPassword()).isEqualTo("123"); // test alias lectura
    }
}
