package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.LoginDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        boolean autenticado = clienteService.verificarCredenciales(loginDTO.getEmail(), loginDTO.getPassword());
        if (autenticado) {
            Cliente cliente = clienteService.getClienteByEmail(loginDTO.getEmail());
            String token = jwtUtil.generateToken(cliente.getEmail(), cliente.getRol());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("rol", cliente.getRol()); 
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroDTO registroDTO) {
        Cliente nuevoCliente = clienteService.registrarCliente(registroDTO);
        return ResponseEntity.ok(Map.of(
            "id", nuevoCliente.getId(),
            "email", nuevoCliente.getEmail()
        ));
    }
}
