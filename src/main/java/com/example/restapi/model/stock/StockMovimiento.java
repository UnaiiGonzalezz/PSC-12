package com.example.restapi.model.stock;

import com.example.restapi.model.Medicamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_stock")
public class StockMovimiento {

    /* ---------- Campos ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_medicamento")
    private Medicamento medicamento;

    @Enumerated(EnumType.STRING)
    private MovimientoTipo tipo;

    /** Unidades añadidas (+) o retiradas (–) */
    private int cantidad;

    private int stockAntes;
    private int stockDespues;

    private LocalDateTime fecha = LocalDateTime.now();

    /* ---------- Constructores ---------- */
    public StockMovimiento() { }

    public StockMovimiento(Medicamento medicamento, MovimientoTipo tipo,
                           int cantidad, int stockAntes, int stockDespues) {
        this.medicamento = medicamento;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAntes = stockAntes;
        this.stockDespues = stockDespues;
    }

    /* ---------- Getters & Setters ---------- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public MovimientoTipo getTipo() { return tipo; }
    public void setTipo(MovimientoTipo tipo) { this.tipo = tipo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getStockAntes() { return stockAntes; }
    public void setStockAntes(int stockAntes) { this.stockAntes = stockAntes; }

    public int getStockDespues() { return stockDespues; }
    public void setStockDespues(int stockDespues) { this.stockDespues = stockDespues; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    /* ---------- toString ---------- */
    @Override
    public String toString() {
        return "StockMovimiento{" +
                "id=" + id +
                ", medicamento=" + medicamento.getNombre() +
                ", tipo=" + tipo +
                ", cantidad=" + cantidad +
                ", stockAntes=" + stockAntes +
                ", stockDespues=" + stockDespues +
                ", fecha=" + fecha +
                '}';
    }
}
