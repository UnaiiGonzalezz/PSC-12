package com.example.restapi.model.dto;

import com.example.restapi.model.dto.CarritoDTO.ItemDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemDTOTest {

    @Test
    void testGettersAndSetters() {
        ItemDTO item = new ItemDTO();

        item.setMedicamentoId(1L);
        item.setNombre("Ibuprofeno");
        item.setPrecioUnitario(5.5);
        item.setCantidad(2);
        item.setSubtotal(11.0);

        assertThat(item.getMedicamentoId()).isEqualTo(1L);
        assertThat(item.getNombre()).isEqualTo("Ibuprofeno");
        assertThat(item.getPrecioUnitario()).isEqualTo(5.5);
        assertThat(item.getCantidad()).isEqualTo(2);
        assertThat(item.getSubtotal()).isEqualTo(11.0);
    }
}
