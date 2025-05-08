package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.StockMovimientoRepository;
import com.example.restapi.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private StockMovimientoRepository movRepo;

    /* ---------- CRUD ---------- */

    @GetMapping
    public List<MedicamentoDTO> getAllMedicamentos() {
        return medicamentoService.getAllMedicamentos()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody Medicamento medicamento) {
        try {
            Medicamento nuevo = medicamentoService.saveMedicamento(medicamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Medicamento> updateMedicamento(@PathVariable Long id, @RequestBody Medicamento body) {
        try {
            Medicamento actualizado = medicamentoService.updateMedicamento(id, body);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.deleteMedicamento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medicamento no encontrado");
        }
    }

    /* ---------- Paginado para catálogo ---------- */

    @GetMapping("/pag")
    public Page<MedicamentoDTO> getMedicamentosPaginados(Pageable pageable) {
        return medicamentoService.getMedicamentos(pageable);
    }

    /* ---------- Filtros ---------- */

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

    /* ---------- Disponibles ---------- */

    @GetMapping("/disponibles")
    public List<MedicamentoDTO> getMedicamentosDisponibles() {
        return medicamentoService.getMedicamentosDisponibles();
    }

    /* ---------- Movimientos de stock ---------- */

    @GetMapping("/{id}/movimientos")
    public List<StockMovimiento> getMovimientos(@PathVariable Long id) {
        Medicamento med = medicamentoService.getMedicamentoById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento no encontrado"));
        return movRepo.findByMedicamentoOrderByFechaDesc(med);
    }

    /* ---------- Buscar medicamento por ID ---------- */

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> getMedicamentoById(@PathVariable Long id) {
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(id);
        return medicamento.map(m -> ResponseEntity.ok(convertToDTO(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ---------- Conversor privado Medicamento -> MedicamentoDTO ---------- */

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
