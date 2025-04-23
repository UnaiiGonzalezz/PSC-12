package com.example.restapi.model.dto;

public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String metodoPago;

    public ClienteDTO() { }

    public ClienteDTO(Long id, String nombre, String apellido,
                      String email, String metodoPago) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.metodoPago = metodoPago;
    }

    /* getters & setters … */
}
