package com.example.restapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- Relaciones --- */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_medicamento", nullable = false)
    private Medicamento medicamento;

    /* --- Atributos --- */
    @Column(nullable = false)
    private int cantidad;

    /* --- Constructores --- */
    public CarritoItem() {
        // Constructor vacío obligatorio para JPA
    }

    public CarritoItem(Carrito carrito, Medicamento medicamento, int cantidad) {
        this.carrito = carrito;
        this.medicamento = medicamento;
        this.cantidad = cantidad;
    }

    /* --- Métodos de dominio --- */

    public double getSubtotal() {
        if (medicamento != null) {
            return medicamento.getPrecio() * cantidad;
        }
        return 0.0;
    }

    /* --- Getters y Setters --- */

    public Long getId() {
        return id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
