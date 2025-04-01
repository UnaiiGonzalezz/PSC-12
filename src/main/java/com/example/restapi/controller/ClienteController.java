package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        System.out.println("Credenciales recibidas: " + credenciales); // DEBUG

        String email = credenciales.get("email");
        String contrasena = credenciales.get("contraseña");

        if (email == null || contrasena == null) {
            return ResponseEntity.badRequest().body("Email y contraseña son requeridos.");
        }

        boolean valido = clienteService.verificarCredenciales(email, contrasena);

        if (valido) {
            return ResponseEntity.ok().body("Inicio de sesión exitoso.");
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas.");
        }
    }*/
}

