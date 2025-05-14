package com.example.restapi.model.dto;


import java.time.LocalDate;
import java.util.List;

public class CompraDTO {

    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private String apellidoCliente;
    private List<MedicamentoDTO> medicamentos;
    private LocalDate fechaCompra;
    private int cantidad;
    private double pago;
    private String estado;
    private String metodoPago;

    public CompraDTO() {}

    public CompraDTO(Long id, Long clienteId, String nombreCliente, String apellidoCliente,
                     List<MedicamentoDTO> medicamentos, LocalDate fechaCompra, int cantidad,
                     double pago, String estado, String metodoPago) {
        this.id = id;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.medicamentos = medicamentos;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.pago = pago;
        this.estado = estado;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public List<MedicamentoDTO> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<MedicamentoDTO> medicamentos) { this.medicamentos = medicamentos; }

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
}
 
    

