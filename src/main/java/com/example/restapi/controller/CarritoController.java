package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{email}")
    public Cliente obtenerCarritoPorEmail(@PathVariable String email) {
        return clienteService.getClienteByEmail(email);  // CORREGIDO
    }
}
