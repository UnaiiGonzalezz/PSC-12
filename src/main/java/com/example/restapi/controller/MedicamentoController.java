package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    // Obtener todos
    @GetMapping
    public List<MedicamentoDTO> getAllMedicamentos() {
        return medicamentoService.getAllMedicamentos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> getMedicamentoById(@PathVariable Long id) {
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(id);
        return medicamento.map(m -> ResponseEntity.ok(convertToDTO(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody Medicamento medicamento) {
        Medicamento nuevo = medicamentoService.saveMedicamento(medicamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // Actualizar existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medicamento> updateMedicamento(@PathVariable Long id, @RequestBody Medicamento body) {
        try {
            Medicamento actualizado = medicamentoService.updateMedicamento(id, body);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.deleteMedicamento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medicamento no encontrado");
        }
    }

    // Paginado
    @GetMapping("/pag")
    public Page<MedicamentoDTO> getMedicamentosPaginados(Pageable pageable) {
        return medicamentoService.getMedicamentos(pageable);
    }

    // Filtros
    @GetMapping("/nombre/{nombre}")
    public List<MedicamentoDTO> getMedicamentosPorNombre(@PathVariable String nombre) {
        return medicamentoService.getMedicamentosPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/categoria/{categoria}")
    public List<MedicamentoDTO> getMedicamentosPorCategoria(@PathVariable String categoria) {
        return medicamentoService.getMedicamentosPorCategoria(categoria)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Medicamentos disponibles
    @GetMapping("/disponibles")
    public List<MedicamentoDTO> getMedicamentosDisponibles() {
        return medicamentoService.getMedicamentosDisponibles();
    }

    // Movimientos de stock
    @GetMapping("/{id}/movimientos")
    public List<StockMovimiento> getMovimientos(@PathVariable Long id) {
        return medicamentoService.getMovimientosDeMedicamento(id);
    }

    // Conversión a DTO
    private MedicamentoDTO convertToDTO(Medicamento m) {
        return new MedicamentoDTO(
                m.getId(),
                m.getNombre(),
                m.getPrecio(),
                m.getCategoria(),
                m.getStock(),
                m.getProveedor(),
                m.isDisponible()
        );
    }
}
