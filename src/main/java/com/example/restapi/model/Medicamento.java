package main.java.com.example.restapi.model;

import java.util.ArrayList;
import java.util.List;

public class Medicamento {
    private Long id;               // Identificador del medicamento
    private String nombre;         // Nombre del medicamento
    private String tipo;           // Tipo de medicamento (Ej: "Analgésico", "Antibiótico")
    private double precio;         // Precio del medicamento
    private int stock;             // Cantidad disponible en la farmacia
    private String proveedor;      // Nombre del proveedor del medicamento
    private List<Compra> compras;    // Lista de compras asociadas a este medicamento

    
    public Medicamento(Long id, String nombre, String tipo, double precio, int stock, String proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.stock = stock;
        this.proveedor = proveedor;
        this.compras = new ArrayList<>();
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public List<Compra> getCompras() { return compras; }
    public void addCompra(Compra compra) { this.compras.add(compra); }

    @Override
    public String toString() {
        return "Medicamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", proveedor='" + proveedor + '\'' +
                '}';
    }
}
