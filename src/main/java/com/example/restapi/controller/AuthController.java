package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.LoginDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.service.ClienteService;
import com.example.restapi.security.JwtService;  // 👈 Necesitamos el servicio que genera el token

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")  // 👈 Lo dejamos en /auth para que sea corto
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtService jwtService;  // 👈 Inyectamos el servicio de JWT

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        boolean autenticado = clienteService.verificarCredenciales(loginDTO.getEmail(), loginDTO.getPassword());
        if (autenticado) {
            String token = jwtService.generateToken(loginDTO.getEmail());  // 👈 Creamos el token usando el email

            Map<String, String> response = new HashMap<>();
            response.put("token", token);  // 👈 Devolvemos el token en un JSON
            return ResponseEntity.ok(response);
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
