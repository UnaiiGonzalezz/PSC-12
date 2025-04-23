package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.*;
import com.example.restapi.security.JwtUtil;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;
    @Autowired 
    private JwtUtil jwtUtil;

    /* ---------- POST /auth/login ---------- */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        boolean ok = clienteService.verificarCredenciales(
                body.getEmail(), body.getContrasena());

        if (!ok) {
            // credenciales incorrectas
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");
        }

        // obtener Cliente para devolver datos públicos
        Cliente cliente = clienteService.getClienteById(
                clienteService.getClienteByEmail(body.getEmail()).get().getId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error recuperando cliente"));

        ClienteDTO dto = new ClienteDTO(
                cliente.getId(), cliente.getNombre(), cliente.getApellido(),
                cliente.getEmail(), cliente.getMetodoPago());

        return new LoginResponseDTO("Login correcto", dto);
    }

    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        boolean ok = clienteService.verificarCredenciales(body.getEmail(), body.getContrasena());
        if (!ok)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");

        Cliente c = clienteService.getClienteByEmail(body.getEmail()).get();
        ClienteDTO dto = new ClienteDTO(c.getId(), c.getNombre(), c.getApellido(),
                                        c.getEmail(), c.getMetodoPago());

        String token = jwtUtil.generateToken(c.getEmail());

        return new LoginResponseDTO(token, dto);   // ← token en la respuesta
    }
}
