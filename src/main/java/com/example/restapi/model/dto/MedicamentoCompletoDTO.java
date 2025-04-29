package com.example.restapi.model.dto;

public class MedicamentoCompletoDTO {
    private Long id;
    private String nombre;
    private double precio;
    private String categoria;
    private int stock;
    private String proveedor;
    private boolean disponible;

    public MedicamentoCompletoDTO(Long id, String nombre, double precio, String categoria, int stock, String proveedor, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.proveedor = proveedor;
        this.disponible = disponible;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public int getStock() { return stock; }
    public String getProveedor() { return proveedor; }
    public boolean isDisponible() { return disponible; }
}
