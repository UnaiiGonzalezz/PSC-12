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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MedicamentoDTO> getAllMedicamentos() {
        return medicamentoService.getAllMedicamentos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicamentoDTO> getMedicamentoById(@PathVariable Long id) {
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(id);
        return medicamento.map(m -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(convertToDTO(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody Medicamento medicamento) {
        Medicamento nuevo = medicamentoService.saveMedicamento(medicamento);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(nuevo);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Medicamento> updateMedicamento(@PathVariable Long id, @RequestBody Medicamento body) {
        try {
            Medicamento actualizado = medicamentoService.updateMedicamento(id, body);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.deleteMedicamento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicamento no encontrado");
        }
    }

    @GetMapping(value = "/pag", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<MedicamentoDTO> getMedicamentosPaginados(Pageable pageable) {
        return medicamentoService.getMedicamentos(pageable);
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MedicamentoDTO> getMedicamentosPorNombre(@PathVariable String nombre) {
        return medicamentoService.getMedicamentosPorNombre(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categoria/{categoria}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MedicamentoDTO> getMedicamentosPorCategoria(@PathVariable String categoria) {
        return medicamentoService.getMedicamentosPorCategoria(categoria)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/disponibles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MedicamentoDTO> getMedicamentosDisponibles() {
        return medicamentoService.getMedicamentosDisponibles();
    }

    @GetMapping(value = "/{id}/movimientos", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StockMovimiento> getMovimientos(@PathVariable Long id) {
        return medicamentoService.getMovimientosDeMedicamento(id);
    }

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
