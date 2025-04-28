package com.example.restapi.model.dto;

/**
 * DTO para representar los datos esenciales de un medicamento.
 * Solo expone los atributos necesarios para la visualización.
 */
public class MedicamentoDTO {

    private Long id;         // ID del medicamento
    private String nombre;   // Nombre del medicamento
    private Double precio;   // Precio del medicamento

    public MedicamentoDTO() {
        // Constructor vacío necesario para deserialización
    }

    public MedicamentoDTO(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    /* --- Getters y Setters --- */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "MedicamentoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
