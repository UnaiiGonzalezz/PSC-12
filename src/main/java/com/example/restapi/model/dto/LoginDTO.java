package com.example.restapi.model.dto;

/**
 * DTO para capturar los datos de inicio de sesión.
 */
public class LoginDTO {
    private String email;
    private String password;  // Usamos el nombre estándar "password"

    public LoginDTO() { }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
