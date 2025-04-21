package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.LoginDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;

        @PostMapping("/registro")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistroDTO registroDTO) {
        try {
            Cliente nuevoCliente = clienteService.registrarCliente(registroDTO);
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}/compras")
    public ResponseEntity<List<Compra>> obtenerComprasPorCliente(@PathVariable Long id) {
        List<Compra> compras = clienteService.obtenerComprasPorCliente(id);
        return ResponseEntity.ok(compras);
    }

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    if (loginDTO.getEmail() == null || loginDTO.getContrasena() == null) {
        return ResponseEntity.badRequest().body("Email y contraseña son requeridos.");
    }

    boolean valido = clienteService.verificarCredenciales(loginDTO.getEmail(), loginDTO.getContrasena());

    if (valido) {
        return ResponseEntity.ok().body("Inicio de sesión exitoso.");
    } else {
        return ResponseEntity.status(401).body("Credenciales incorrectas.");
    }
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

