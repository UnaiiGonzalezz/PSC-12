package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.dto.CompraDTO;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.service.ClienteService;
import com.example.restapi.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

// Obtener todas
    @GetMapping
    public List<CompraDTO> getAllCompras() {
        return compraService.getAllCompras()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ POST: Crear compra desde email
    @PostMapping("/crear-por-email")
    public ResponseEntity<Map<String, Object>> crearCompraDesdeEmail(@RequestBody CompraRequest request) {
        Cliente cliente = clienteService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Medicamento> medicamentos = medicamentoRepository.findAllById(request.getMedicamentoIds());

        if (medicamentos.isEmpty()) {
            throw new RuntimeException("No se encontraron medicamentos con los IDs dados.");
        }

        Compra compra = new Compra(cliente, medicamentos,
                LocalDate.now(), request.getCantidad(), request.getMetodoPago());

        double total = medicamentos.stream()
                .mapToDouble(Medicamento::getPrecio)
                .sum();

        compra.setPago(total * request.getCantidad());

        Compra compraGuardada = compraService.saveCompra(compra);

        Map<String, Object> response = new HashMap<>();
        response.put("id", compraGuardada.getId());

        return ResponseEntity.ok(response);
    }

    // ✅ GET: Obtener detalles de compra (estado)
    @GetMapping("/{id}")
    public ResponseEntity<?> getEstadoCompra(@PathVariable Long id) {
        return compraService.getEstadoCompraDTO(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ PATCH: Cambiar estado de una compra
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Compra> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Compra actualizada = compraService.updateEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ Clase auxiliar para recibir JSON en POST
    public static class CompraRequest {
        private String email;
        private List<Long> medicamentoIds;
        private int cantidad;
        private String metodoPago;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public List<Long> getMedicamentoIds() { return medicamentoIds; }
        public void setMedicamentoIds(List<Long> medicamentoIds) { this.medicamentoIds = medicamentoIds; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    }

    // Conversión a DTO
    private CompraDTO convertToDTO(Compra c) {
    return new CompraDTO(
        c.getId(),
        c.getCliente().getId(),
        c.getCliente().getNombre(),
        c.getCliente().getApellido(),
        c.getMedicamentos().stream()
            .map(m -> new MedicamentoDTO(
                m.getId(),
                m.getNombre(),
                m.getPrecio(),
                m.getCategoria(),
                m.getStock(),
                m.getProveedor(),
                m.isDisponible()
            ))
            .toList(),
        c.getFechaCompra(),
        c.getCantidad(),
        c.getPago(),
        c.getEstado(),
        c.getMetodoPago()
    );
    }
}
