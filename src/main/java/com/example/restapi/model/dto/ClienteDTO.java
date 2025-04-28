package com.example.restapi.model.dto;

public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String metodoPago;
    private String rol; // ✅ Añadido campo rol

    public ClienteDTO() { }

    public ClienteDTO(Long id, String nombre, String apellido, String email, String metodoPago, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.metodoPago = metodoPago;
        this.rol = rol;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getRol() {
        return rol;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
