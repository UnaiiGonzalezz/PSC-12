package com.example.restapi.model.dto;

/**
 * DTO para representar los datos esenciales de un medicamento.
 * Solo expone los atributos necesarios para la visualización.
 */
public class MedicamentoDTO {

    private Long id;           // Identificador único del medicamento
    private String nombre;     // Nombre del medicamento
    private double precio;     // Precio del medicamento

    /**
     * Constructor sin argumentos (necesario para deserialización de JSON).
     */
    public MedicamentoDTO() { }

    /**
     * Constructor con parámetros para inicializar el DTO.
     * @param id El identificador único del medicamento.
     * @param nombre El nombre del medicamento.
     * @param precio El precio del medicamento.
     */
    public MedicamentoDTO(Long id, String nombre, double precio) {
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Método toString para representar el DTO en formato String.
     * @return Cadena representando el DTO de Medicamento.
     */
    @Override
    public String toString() {
        return "MedicamentoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
