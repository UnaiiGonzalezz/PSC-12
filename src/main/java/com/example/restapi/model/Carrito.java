package com.example.restapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Carrito de la compra asociado a un cliente.
 * Contiene una colección de CarritoItem.
 */
@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* --- Relaciones --- */

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    /* --- Otros campos --- */

    @Column(nullable = false)
    private LocalDateTime creado = LocalDateTime.now();

    /* --- Constructores --- */

    public Carrito() { }

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
    }

    /* --- Lógica de dominio --- */

    public void addItem(Medicamento medicamento, int cantidad) {
        // ¿Ya existe el medicamento en el carrito?
        items.stream()
             .filter(ci -> ci.getMedicamento().getId().equals(medicamento.getId()))
             .findFirst()
             .ifPresentOrElse(
                 ci -> ci.setCantidad(ci.getCantidad() + cantidad),
                 () -> items.add(new CarritoItem(this, medicamento, cantidad))
             );
    }

    public void removeItem(Long medicamentoId) {
        items.removeIf(ci -> ci.getMedicamento().getId().equals(medicamentoId));
    }

    public double getTotal() {
        return items.stream()
                    .mapToDouble(CarritoItem::getSubtotal)
                    .sum();
    }

    /* --- Getters / Setters --- */

    public Long getId() { return id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<CarritoItem> getItems() { return items; }
    public void setItems(List<CarritoItem> items) { this.items = items; }

    public LocalDateTime getCreado() { return creado; }
    public void setCreado(LocalDateTime creado) { this.creado = creado; }
}
