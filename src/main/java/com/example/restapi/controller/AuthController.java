package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.LoginDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        boolean autenticado = clienteService.verificarCredenciales(loginDTO.getEmail(), loginDTO.getPassword());
        if (autenticado) {
            Cliente cliente = clienteService.getClienteByEmail(loginDTO.getEmail());
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Cliente> register(@RequestBody RegistroDTO registroDTO) {
        Cliente nuevoCliente = clienteService.registrarCliente(registroDTO);
        return ResponseEntity.ok(nuevoCliente);
    }
}