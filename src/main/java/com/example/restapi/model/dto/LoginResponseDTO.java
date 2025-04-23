package com.example.restapi.model.dto;

public class LoginResponseDTO {

    private String token;      // ← el JWT
    private ClienteDTO cliente;

    public LoginResponseDTO() { }

    public LoginResponseDTO(String token, ClienteDTO cliente) {
        this.token   = token;
        this.cliente = cliente;
    }

    /* ----- getters & setters ----- */

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
}
