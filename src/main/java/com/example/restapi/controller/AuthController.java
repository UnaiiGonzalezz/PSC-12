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

    @Autowired private ClienteService clienteService;
    @Autowired private JwtUtil        jwtUtil;

    /* ---------- POST /auth/login ---------- */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        boolean ok = clienteService.verificarCredenciales(body.getEmail(), body.getContrasena());
        if (!ok)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos");

        Cliente c   = clienteService.getClienteByEmail(body.getEmail()).get();
        String token = jwtUtil.generateToken(c.getEmail());

        return new LoginResponseDTO(token,
                new ClienteDTO(c.getId(), c.getNombre(), c.getApellido(),
                               c.getEmail(), c.getMetodoPago()));
    }

    /* ---------- OPCIONAL: POST /auth/registro ---------- */
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO registro(@RequestBody RegistroDTO body) {
        // crea cliente
        Cliente nuevo = clienteService.registrarCliente(body);

        // token inmediato
        String token = jwtUtil.generateToken(nuevo.getEmail());

        return new LoginResponseDTO(token,
                new ClienteDTO(nuevo.getId(), nuevo.getNombre(), nuevo.getApellido(),
                               nuevo.getEmail(), nuevo.getMetodoPago()));
    }
}
