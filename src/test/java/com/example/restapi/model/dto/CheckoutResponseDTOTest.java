package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        CheckoutResponseDTO obj = new CheckoutResponseDTO();
        obj.setCompraId(null);
        assertThat(obj.getCompraId()).isEqualTo(null);
        obj.setFechaCompra(null);
        assertThat(obj.getFechaCompra()).isEqualTo(null);
        obj.setEstado("test");
        assertThat(obj.getEstado()).isEqualTo("test");
        obj.setTotal(null);
        assertThat(obj.getTotal()).isEqualTo(null);
        obj.setItems(null);
        assertThat(obj.getItems()).isEqualTo(null);
        obj.setNombre("test");
        assertThat(obj.getNombre()).isEqualTo("test");
        obj.setPrecioUnitario(null);
        assertThat(obj.getPrecioUnitario()).isEqualTo(null);
        obj.setCantidad(1);
        assertThat(obj.getCantidad()).isEqualTo(1);
        obj.setSubtotal(null);
        assertThat(obj.getSubtotal()).isEqualTo(null);
    }
}
