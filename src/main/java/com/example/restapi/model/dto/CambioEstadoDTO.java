package com.example.restapi.model.dto;

/** Petición para cambiar el estado de una compra. */
public class CambioEstadoDTO {
    private String estado;   // Pendiente, Enviado, Entregado, Cancelada

    public CambioEstadoDTO() { }

    public CambioEstadoDTO(String estado) { this.estado = estado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
