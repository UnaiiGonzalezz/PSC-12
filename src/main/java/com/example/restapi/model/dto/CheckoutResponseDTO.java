package com.example.restapi.model.dto;

import java.time.LocalDate;
import java.util.List;

/** Respuesta detallada tras confirmar el carrito. */
public class CheckoutResponseDTO {

    private Long compraId;
    private LocalDate fechaCompra;
    private String estado;
    private double total;
    private List<ItemDTO> items;

    public CheckoutResponseDTO() { }

    public CheckoutResponseDTO(Long compraId, LocalDate fechaCompra,
                               String estado, double total, List<ItemDTO> items) {
        this.compraId = compraId;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.total = total;
        this.items = items;
    }

    /* ---------- Getters & Setters ---------- */

    public Long getCompraId() { return compraId; }
    public void setCompraId(Long compraId) { this.compraId = compraId; }

    public LocalDate getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    /* ---------- DTO interno ---------- */
    public static class ItemDTO {
        private String nombre;
        private double precioUnitario;
        private int cantidad;
        private double subtotal;

        public ItemDTO() { }

        public ItemDTO(String nombre, double precioUnitario,
                       int cantidad, double subtotal) {
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }

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
