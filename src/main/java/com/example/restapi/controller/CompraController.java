package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compras") // <- cambiado para que encaje con el test
public class CompraController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CompraService compraService;

    // Esta ya estaba, pero ajustamos el path
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Compra>> comprasPorCliente(@PathVariable Long clienteId) {
        Cliente cliente = clienteService.getClienteById(clienteId);
        List<Compra> compras = compraService.findByCliente(cliente);
        return ResponseEntity.ok(compras);
    }

    // 🚀 Nuevo endpoint para actualizar el estado de una compra
    @PatchMapping("/{compraId}/estado")
    public ResponseEntity<EstadoCompraDTO> cambiarEstado(@PathVariable Long compraId, @RequestBody EstadoCompraDTO estadoCompraDTO) {
        Compra compra = compraService.updateEstado(compraId, estadoCompraDTO.getEstado());
        Optional<EstadoCompraDTO> updatedDto = compraService.getEstadoCompraDTO(compra.getId());

        if (updatedDto.isPresent()) {
            return ResponseEntity.ok(updatedDto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
