package com.example.restapi.model.dto;

public class LoginDTO {
    private String email;
    private String contrasena;

    public LoginDTO() {}

    public LoginDTO(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // AÑADIR ESTE MÉTODO:
    public String getPassword() {
        return contrasena;  // Mapea getPassword() a contrasena
    }
}
