package com.example.restapi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;
 
    @ManyToMany
    @JoinTable(
        name = "compra_medicamento",
        joinColumns = @JoinColumn(name = "idCompra"),
        inverseJoinColumns = @JoinColumn(name = "idMedicamento")
    )
    private List<Medicamento> medicamentos;
 
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
 
    public Compra() {
        this.medicamentos = new ArrayList<>(); // Inicializamos la lista de medicamentos
    }

    public Compra(Cliente cliente, List<Medicamento> medicamentos, LocalDate fechaCompra, int cantidad, String metodoPago) {
        this.cliente = cliente;
        this.medicamentos = medicamentos;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.metodoPago = metodoPago;
        this.estado = "Pendiente"; // Inicalmente la compra esta como pendiente
        this.pago = calcularTotal(); 
    }

    // Calcular el pago sumando el precio de todos los medicamentos
    private double calcularTotal() {
        return medicamentos.stream().mapToDouble(Medicamento::getPrecio).sum() * cantidad;
    }

    
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


    // Método para confirmar la compra
    public void confirmarCompra() {
        this.estado = "Enviado";
    }

    // Método para marcar la compra como entregada
    public void entregarCompra() {
        this.estado = "Entregado";
    }

    // Método para cancelar la compra
    public void cancelarCompra() {
        this.estado = "Cancelada";
    }
    
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


