package com.example.restapi.model.dto;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

@Tag("unit")
public class CarritoDTOTest {

    @Test
    void testGettersAndSetters() {
        CarritoDTO obj = new CarritoDTO();

        obj.setClienteId(1L);
        obj.setItems(Collections.emptyList());
        obj.setTotal(10.0);

        assertThat(obj.getClienteId()).isEqualTo(1L);
        assertThat(obj.getItems()).isEqualTo(Collections.emptyList());
        assertThat(obj.getTotal()).isEqualTo(10.0);
    }
}
