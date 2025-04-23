package com.example.restapi.model.dto;

import java.time.LocalDate;

/**
 * DTO ligero para mostrar un resumen de compras en el historial.
 */
public class CompraResumenDTO {

    private Long id;
    private LocalDate fechaCompra;
    private String estado;
    private double total;

    public CompraResumenDTO() { }

    public CompraResumenDTO(Long id, LocalDate fechaCompra,
                            String estado, double total) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.estado = estado;
        this.total = total;
    }

    /* getters & setters */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
