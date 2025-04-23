package com.example.restapi.controller;

import com.example.restapi.model.Compra;
import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.*;
import com.example.restapi.security.SecurityUtil;
import com.example.restapi.service.CompraService;
import com.example.restapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired private CompraService     compraService;
    @Autowired private ClienteRepository clienteRepo;

    /* ---------- CRUD básico ---------- */
    @GetMapping
    public List<Compra> getAllCompras() {
        return compraService.getAllCompras();
    }

    @PostMapping
    public Compra createCompra(@RequestBody Compra compra) {
        return compraService.saveCompra(compra);
    }

    /* ---------- Estado detallado ---------- */
    @GetMapping("/{id}/estado")
    public EstadoCompraDTO getEstadoCompra(@PathVariable Long id) {
        return compraService.getEstadoCompraDTO(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Compra no encontrada"));
    }

    /* ---------- Historial por ID de cliente (se mantiene) ---------- */
    @GetMapping("/cliente/{clienteId}")
    public List<CompraResumenDTO> getHistorialCliente(@PathVariable Long clienteId) {
        return compraService.getHistorialPorCliente(clienteId);
    }

    /* ---------- NUEVO: /compras/mias (historial del cliente autenticado) ---------- */
    @GetMapping("/mias")
    public List<CompraResumenDTO> misCompras() {
        String email = SecurityUtil.getCurrentUserEmail();
        Cliente cliente = clienteRepo.findByEmail(email);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        return compraService.getHistorialPorCliente(cliente.getId());
    }

    /* ---------- Cambiar estado de una compra ---------- */
    @PatchMapping("/{id}/estado")
    public EstadoCompraDTO cambiarEstado(@PathVariable Long id,
                                         @RequestBody CambioEstadoDTO body) {
        try {
            compraService.updateEstado(id, body.getEstado());
            return compraService.getEstadoCompraDTO(id).get();
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
