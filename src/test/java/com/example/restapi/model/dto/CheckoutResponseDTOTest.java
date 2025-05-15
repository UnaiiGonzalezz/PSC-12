package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutResponseDTOTest {

    @Test
    void testConstructorAndGetters() {
        CheckoutResponseDTO.ItemDTO item = new CheckoutResponseDTO.ItemDTO("Aspirina", 1.5, 2, 3.0);
        CheckoutResponseDTO dto = new CheckoutResponseDTO(10L, LocalDate.of(2024, 5, 15), "PAGADO", 3.0, Collections.singletonList(item));

        assertEquals(10L, dto.getCompraId());
        assertEquals(LocalDate.of(2024, 5, 15), dto.getFechaCompra());
        assertEquals("PAGADO", dto.getEstado());
        assertEquals(3.0, dto.getTotal());
        assertEquals(1, dto.getItems().size());
        assertEquals(item, dto.getItems().get(0));
    }

    @Test
    void testItemDTOSetters() {
        CheckoutResponseDTO.ItemDTO item = new CheckoutResponseDTO.ItemDTO();
        item.setNombre("Vitamina C");
        item.setPrecioUnitario(2.0);
        item.setCantidad(3);
        item.setSubtotal(6.0);

        assertEquals("Vitamina C", item.getNombre());
        assertEquals(2.0, item.getPrecioUnitario());
        assertEquals(3, item.getCantidad());
        assertEquals(6.0, item.getSubtotal());
    }
    
    @Test
    void testSetterCompleto() {
        CheckoutResponseDTO dto = new CheckoutResponseDTO();
        dto.setCompraId(20L);
        dto.setFechaCompra(LocalDate.of(2023, 12, 31));
        dto.setEstado("ENTREGADO");
        dto.setTotal(50.0);
        dto.setItems(Collections.emptyList());

        assertEquals(20L, dto.getCompraId());
        assertEquals("ENTREGADO", dto.getEstado());
        assertEquals(50.0, dto.getTotal());
        assertEquals(0, dto.getItems().size());
    }
}
