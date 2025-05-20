package com.example.restapi.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MedicamentoTest {

    @Test
    void testConstructorYGetters() {
        Medicamento med = new Medicamento("Ibuprofeno", "Antiinflamatorio", 3.5, 20, "Bayer");

        med.setId(10L);

        assertThat(med.getId()).isEqualTo(10L);
        assertThat(med.getNombre()).isEqualTo("Ibuprofeno");
        assertThat(med.getCategoria()).isEqualTo("Antiinflamatorio");
        assertThat(med.getPrecio()).isEqualTo(3.5);
        assertThat(med.getStock()).isEqualTo(20);
        assertThat(med.getProveedor()).isEqualTo("Bayer");
        assertThat(med.isDisponible()).isTrue();
    }

    @Test
    void testSettersIndependientes() {
        Medicamento med = new Medicamento();

        med.setId(5L);
        med.setNombre("Paracetamol");
        med.setCategoria("Analgésico");
        med.setPrecio(2.0);
        med.setStock(15);
        med.setProveedor("Cinfa");
        med.setDisponible(false);

        assertThat(med.getId()).isEqualTo(5L);
        assertThat(med.getNombre()).isEqualTo("Paracetamol");
        assertThat(med.getCategoria()).isEqualTo("Analgésico");
        assertThat(med.getPrecio()).isEqualTo(2.0);
        assertThat(med.getStock()).isEqualTo(15);
        assertThat(med.getProveedor()).isEqualTo("Cinfa");
        assertThat(med.isDisponible()).isFalse();
    }

    @Test
    void testToString() {
        Medicamento med = new Medicamento("Amoxicilina", "Antibiótico", 5.5, 10, "Normon");
        med.setId(1L);
        String result = med.toString();

        assertThat(result).contains("Amoxicilina");
        assertThat(result).contains("Antibiótico");
        assertThat(result).contains("Normon");
        assertThat(result).contains("disponible=true");
    }
}
