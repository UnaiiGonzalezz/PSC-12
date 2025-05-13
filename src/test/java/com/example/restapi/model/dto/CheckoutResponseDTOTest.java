package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        CheckoutResponseDTO dto = new CheckoutResponseDTO();
        dto.setCompraId(100L);
        dto.setFechaCompra(LocalDate.of(2024, 1, 1));
        dto.setEstado("Enviado");
        dto.setTotal(50.5);
        dto.setItems(Collections.emptyList());

        assertThat(dto.getCompraId()).isEqualTo(100L);
        assertThat(dto.getFechaCompra()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(dto.getEstado()).isEqualTo("Enviado");
        assertThat(dto.getTotal()).isEqualTo(50.5);
        assertThat(dto.getItems()).isEqualTo(Collections.emptyList());
    }
}
