package com.example.restapi.model.dto;

import java.util.List;

/**
 * DTO para exponer el contenido completo de un carrito.
 */
public class CarritoDTO {

    private Long clienteId;
    private List<ItemDTO> items;
    private double total;

    public CarritoDTO(Long clienteId, List<ItemDTO> items, double total) {
        this.clienteId = clienteId;
        this.items = items;
        this.total = total;
    }

    public CarritoDTO() { }

    /* ---------- Getters & Setters ---------- */

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    /* ---------- Sub-DTO para cada línea ---------- */

    public static class ItemDTO {
        private Long medicamentoId;
        private String nombre;
        private double precioUnitario;
        private int cantidad;
        private double subtotal;

        public ItemDTO(Long medicamentoId, String nombre,
                       double precioUnitario, int cantidad, double subtotal) {
            this.medicamentoId = medicamentoId;
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }

        public ItemDTO() { }

        public Long getMedicamentoId() { return medicamentoId; }
        public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
}
