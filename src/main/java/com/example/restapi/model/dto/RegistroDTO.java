package com.example.restapi.model.dto;

public class RegistroDTO {
    private String nombre;
    private String email;
    private String contrasena;

    // Opcionales
    private String apellido = "";
    private String telefono = "";
    private String metodoPago = "No especificado";

    // 🔵 Getters y Setters
    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getContrasena() { 
        return contrasena; 
    }
    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }

    // ✅ Alias para evitar problemas: algunos métodos buscaban getPassword()
    public String getPassword() {
        return contrasena;
    }

    public String getApellido() { 
        return apellido; 
    }
    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }

    public String getTelefono() { 
        return telefono; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getMetodoPago() { 
        return metodoPago; 
    }
    public void setMetodoPago(String metodoPago) { 
        this.metodoPago = metodoPago; 
    }
}
