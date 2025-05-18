package com.example.restapi.model.dto;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class CompraDTOTest {

    @Test
    void testConstructorAndGetters() {
        Long id = 1L;
        Long clienteId = 2L;
        String nombre = "Juan";
        String apellido = "Pérez";
        List<MedicamentoDTO> medicamentos = Collections.emptyList();
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        int cantidad = 5;
        double pago = 99.99;
        String estado = "COMPLETADO";
        String metodoPago = "EFECTIVO";

        CompraDTO dto = new CompraDTO(id, clienteId, nombre, apellido, medicamentos, fecha, cantidad, pago, estado, metodoPago);

        assertEquals(id, dto.getId());
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(nombre, dto.getNombreCliente());
        assertEquals(apellido, dto.getApellidoCliente());
        assertEquals(medicamentos, dto.getMedicamentos());
        assertEquals(fecha, dto.getFechaCompra());
        assertEquals(cantidad, dto.getCantidad());
        assertEquals(pago, dto.getPago());
        assertEquals(estado, dto.getEstado());
        assertEquals(metodoPago, dto.getMetodoPago());
    }

    @Test
    void testSettersAndGetters() {
        CompraDTO dto = new CompraDTO();

        Long id = 10L;
        Long clienteId = 20L;
        String nombre = "Ana";
        String apellido = "López";
        List<MedicamentoDTO> medicamentos = Collections.emptyList();
        LocalDate fecha = LocalDate.of(2025, 5, 15);
        int cantidad = 10;
        double pago = 199.50;
        String estado = "PENDIENTE";
        String metodoPago = "TARJETA";

        dto.setId(id);
        dto.setClienteId(clienteId);
        dto.setNombreCliente(nombre);
        dto.setApellidoCliente(apellido);
        dto.setMedicamentos(medicamentos);
        dto.setFechaCompra(fecha);
        dto.setCantidad(cantidad);
        dto.setPago(pago);
        dto.setEstado(estado);
        dto.setMetodoPago(metodoPago);

        assertEquals(id, dto.getId());
        assertEquals(clienteId, dto.getClienteId());
        assertEquals(nombre, dto.getNombreCliente());
        assertEquals(apellido, dto.getApellidoCliente());
        assertEquals(medicamentos, dto.getMedicamentos());
        assertEquals(fecha, dto.getFechaCompra());
        assertEquals(cantidad, dto.getCantidad());
        assertEquals(pago, dto.getPago());
        assertEquals(estado, dto.getEstado());
        assertEquals(metodoPago, dto.getMetodoPago());
    }
}
