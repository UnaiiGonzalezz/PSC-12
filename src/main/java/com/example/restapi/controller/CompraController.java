package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CompraService compraService;

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Compra>> comprasPorCliente(@PathVariable Long clienteId) {
        Cliente cliente = clienteService.getClienteById(clienteId);
        List<Compra> compras = compraService.findByCliente(cliente);
        return ResponseEntity.ok(compras);
    }
}