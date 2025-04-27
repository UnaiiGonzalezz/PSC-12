package com.example.restapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
        name = "compra_medicamento",
        joinColumns = @JoinColumn(name = "id_compra"),
        inverseJoinColumns = @JoinColumn(name = "id_medicamento")
    )
    private List<Medicamento> medicamentos = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate fechaCompra;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double pago;

    @Column(nullable = false, length = 50)
    private String estado = "Pendiente";

    @Column(nullable = false, length = 50)
    private String metodoPago;

    /* ----- Constructores ----- */
    public Compra() {}

    public Compra(Cliente cliente, List<Medicamento> medicamentos, LocalDate fechaCompra, int cantidad, String metodoPago) {
        this.cliente = cliente;
        this.medicamentos = medicamentos;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.metodoPago = metodoPago;
        this.estado = "Pendiente"; // Estado inicial
        this.pago = calcularTotal();
    }

    /* ----- Métodos de negocio ----- */

    // Calcula el precio total de la compra
    private double calcularTotal() {
        return medicamentos.stream()
                .mapToDouble(Medicamento::getPrecio)
                .sum(); // suma los precios de todos los medicamentos
    }

    public void confirmarCompra() { this.estado = "Enviado"; }

    public void entregarCompra() { this.estado = "Entregado"; }

    public void cancelarCompra() { this.estado = "Cancelada"; }

    /* ----- Getters y Setters ----- */

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<Medicamento> getMedicamentos() { return medicamentos; }

    public void setMedicamentos(List<Medicamento> medicamentos) { this.medicamentos = medicamentos; }

    public LocalDate getFechaCompra() { return fechaCompra; }

    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPago() { return pago; }

    public void setPago(double pago) { this.pago = pago; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }

    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", cliente=" + cliente.getNombre() + " " + cliente.getApellido() +
                ", medicamentos=" + medicamentos.stream().map(Medicamento::getNombre).toList() +
                ", cantidad=" + cantidad +
                ", fechaCompra=" + fechaCompra +
                ", pago=" + pago +
                ", estado='" + estado + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
