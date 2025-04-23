package com.example.restapi.model.dto;

/**
 * DTO sencillo para exponer solo los datos necesarios de un Medicamento.
 */
public class MedicamentoDTO {

    private Long id;
    private String nombre;
    private double precio;

    public MedicamentoDTO() { }

    public MedicamentoDTO(Long id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    /* --- Getters & Setters --- */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
