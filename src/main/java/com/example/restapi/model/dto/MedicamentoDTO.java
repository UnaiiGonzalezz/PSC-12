package com.example.restapi.model.dto;

public class MedicamentoDTO {

    private Long id;
    private String nombre;
    private double precio;
    private String categoria;
    private int stock;
    private String proveedor;
    private boolean disponible;

    public MedicamentoDTO() {}

    public MedicamentoDTO(Long id, String nombre, double precio, String categoria, int stock, String proveedor, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.proveedor = proveedor;
        this.disponible = disponible;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
