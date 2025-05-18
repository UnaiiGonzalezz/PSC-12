package com.example.restapi.model.stock;

import com.example.restapi.model.Medicamento;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class StockMovimientoTest {

    @Test
    void testConstructorYGetters() {
        Medicamento medicamento = new Medicamento("Ibuprofeno", "Analgésico", 3.5, 50, "ProveedorX");
        MovimientoTipo tipo = MovimientoTipo.ENTRADA;
        int cantidad = 10;
        int stockAntes = 50;
        int stockDespues = 60;

        StockMovimiento movimiento = new StockMovimiento(medicamento, tipo, cantidad, stockAntes, stockDespues);

        assertEquals(medicamento, movimiento.getMedicamento());
        assertEquals(tipo, movimiento.getTipo());
        assertEquals(cantidad, movimiento.getCantidad());
        assertEquals(stockAntes, movimiento.getStockAntes());
        assertEquals(stockDespues, movimiento.getStockDespues());
        assertNotNull(movimiento.getFecha());
    }

    @Test
    void testSetters() {
        StockMovimiento movimiento = new StockMovimiento();

        Medicamento medicamento = new Medicamento("Paracetamol", "Analgésico", 2.5, 30, "ProveedorY");
        movimiento.setId(101L);
        movimiento.setMedicamento(medicamento);
        movimiento.setTipo(MovimientoTipo.AJUSTE);
        movimiento.setCantidad(5);
        movimiento.setStockAntes(30);
        movimiento.setStockDespues(25);
        LocalDateTime fecha = LocalDateTime.of(2024, 5, 15, 12, 30);
        movimiento.setFecha(fecha);

        assertEquals(101L, movimiento.getId());
        assertEquals(medicamento, movimiento.getMedicamento());
        assertEquals(MovimientoTipo.AJUSTE, movimiento.getTipo());
        assertEquals(5, movimiento.getCantidad());
        assertEquals(30, movimiento.getStockAntes());
        assertEquals(25, movimiento.getStockDespues());
        assertEquals(fecha, movimiento.getFecha());
    }

    @Test
    void testToString() {
        Medicamento medicamento = new Medicamento("Amoxicilina", "Antibiótico", 7.2, 40, "ProveedorZ");
        StockMovimiento movimiento = new StockMovimiento(medicamento, MovimientoTipo.VENTA, 15, 40, 25);

        String result = movimiento.toString();

        assertTrue(result.contains("Amoxicilina"));
        assertTrue(result.contains("VENTA"));
        assertTrue(result.contains("cantidad=15"));
        assertTrue(result.contains("stockAntes=40"));
        assertTrue(result.contains("stockDespues=25"));
        assertTrue(result.contains("fecha="));
    }
}
