package com.example.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String telefono;
    private String metodoPago;
    private String rol; // "USER" o "ADMIN"

    public Cliente(String nombre, String apellido, String email, String contrasena, String telefono, String metodoPago, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.metodoPago = metodoPago;
        this.rol = rol;
    }

    // ✅ Alias para compatibilidad: setPassword -> realmente setea contrasena
    public void setPassword(String password) {
        this.contrasena = password;
    }

    // ✅ Alias para compatibilidad: getPassword -> realmente devuelve contrasena
    public String getPassword() {
        return this.contrasena;
    }
}
