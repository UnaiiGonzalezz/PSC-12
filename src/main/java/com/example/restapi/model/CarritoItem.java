package com.example.restapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- Relaciones --- */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_medicamento")
    private Medicamento medicamento;

    /* --- Datos --- */
    @Column(nullable = false)
    private int cantidad;

    /* --- Constructores --- */
    public CarritoItem() { }

    public CarritoItem(Carrito carrito, Medicamento medicamento, int cantidad) {
        this.carrito = carrito;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
    }

    /* --- Métodos de dominio --- */

    public double getSubtotal() {
        return medicamento.getPrecio() * cantidad;
    }

    /* --- Getters / Setters --- */

    public Long getId() { return id; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(Carrito carrito) { this.carrito = carrito; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
