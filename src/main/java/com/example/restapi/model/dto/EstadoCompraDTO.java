package com.example.restapi.model.dto;

import java.time.LocalDate;
import java.util.List;

public class EstadoCompraDTO {
    private Long id;
    private String estado;
    private LocalDate fechaCompra;
    private Long clienteId;  // <-- Nuevo campo
    private ClienteInfo cliente;
    private List<MedicamentoInfo> medicamentos;

    public EstadoCompraDTO() {
    }

    public EstadoCompraDTO(Long id, String estado, LocalDate fechaCompra,
                           Long clienteId, ClienteInfo cliente, List<MedicamentoInfo> medicamentos) {
        this.id = id;
        this.estado = estado;
        this.fechaCompra = fechaCompra;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.medicamentos = medicamentos;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public ClienteInfo getCliente() {
        return cliente;
    }

    public void setCliente(ClienteInfo cliente) {
        this.cliente = cliente;
    }

    public List<MedicamentoInfo> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoInfo> medicamentos) {
        this.medicamentos = medicamentos;
    }

    // Subclase interna para cliente
    public static class ClienteInfo {
        private String nombre;
        private String apellido;
        private String email;

        public ClienteInfo() {
        }

        public ClienteInfo(String nombre, String apellido, String email) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // Subclase interna para medicamentos
    public static class MedicamentoInfo {
        private String nombre;
        private double precio;

        public MedicamentoInfo() {
        }

        public MedicamentoInfo(String nombre, double precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
        }
    }
}
