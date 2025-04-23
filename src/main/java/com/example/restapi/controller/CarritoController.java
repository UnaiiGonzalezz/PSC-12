package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.CarritoDTO;
import com.example.restapi.model.dto.CheckoutResponseDTO;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.security.SecurityUtil;
import com.example.restapi.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private CarritoService   carritoService;

    /* ---------- Util ---------- */
    private Cliente getAuthenticatedCliente() {
        String email = SecurityUtil.getCurrentUserEmail();
        return clienteRepo.findByEmail(email);
    }

    /* ---------- Endpoints ---------- */

    // GET /carrito
    @GetMapping
    public CarritoDTO getCarrito() {
        return carritoService.getCarritoDTO(getAuthenticatedCliente());
    }

    // POST /carrito/agregar
    @PostMapping("/agregar")
    public CarritoDTO addItem(@RequestBody AddItemRequest body) {
        return carritoService.addMedicamento(
                getAuthenticatedCliente(), body.getMedicamentoId(), body.getCantidad());
    }

    // DELETE /carrito/eliminar/{medicamentoId}
    @DeleteMapping("/eliminar/{medicamentoId}")
    public CarritoDTO deleteItem(@PathVariable Long medicamentoId) {
        return carritoService.removeMedicamento(getAuthenticatedCliente(), medicamentoId);
    }

    // DELETE /carrito/vaciar
    @DeleteMapping("/vaciar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciar() {
        carritoService.vaciarCarrito(getAuthenticatedCliente());
    }

    // GET /carrito/total
    @GetMapping("/total")
    public double getTotal() {
        return carritoService.getTotal(getAuthenticatedCliente());
    }

    // POST /carrito/checkout
    @PostMapping("/checkout")
    public CheckoutResponseDTO checkout() {
        return carritoService.checkout(getAuthenticatedCliente());
    }

    /* ---------- DTO interno ---------- */
    public static class AddItemRequest {
        private Long medicamentoId;
        private int cantidad;
        public Long getMedicamentoId() { return medicamentoId; }
        public void setMedicamentoId(Long medicamentoId) { this.medicamentoId = medicamentoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}
