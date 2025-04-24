package com.example.restapi.controller;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.StockMovimientoRepository;
import com.example.restapi.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import java.util.List;
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
    public List<Medicamento> getAllMedicamentos() {
        return medicamentoService.getAllMedicamentos();
    }

    /* ---------- NUEVO: GET /medicamentos/pag ---------- */
    @GetMapping("/pag")
    public Page<MedicamentoDTO> getMedicamentosPaginados(Pageable pageable) {
        return medicamentoService.getMedicamentos(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicamento createMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.saveMedicamento(medicamento);
    }

    @PutMapping("/{id}")
    public Medicamento updateMedicamento(@PathVariable Long id,
                                         @RequestBody Medicamento body) {
        try {
            return medicamentoService.updateMedicamento(id, body);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.deleteMedicamento(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Medicamento no encontrado");
        }
    }

    /* ---------- filtros ---------- */

    @GetMapping("/nombre/{nombre}")
    public List<Medicamento> getMedicamentosPorNombre(@PathVariable String nombre) {
        return medicamentoService.getMedicamentosPorNombre(nombre);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Medicamento> getMedicamentosPorCategoria(@PathVariable String categoria) {
        return medicamentoService.getMedicamentosPorCategoria(categoria);
    }

    /* ---------- disponibles ---------- */

    @GetMapping("/disponibles")
    public List<MedicamentoDTO> getMedicamentosDisponibles() {
        return medicamentoService.getMedicamentosDisponibles()
                .stream()
                .map(m -> new MedicamentoDTO(m.getId(), m.getNombre(), m.getPrecio()))
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}/movimientos")
    public List<StockMovimiento> getMovimientos(@PathVariable Long id) {
        Medicamento med = medicamentoService.getMedicamentoById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Medicamento no encontrado"));
        return movRepo.findByMedicamentoOrderByFechaDesc(med);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> getMedicamentoById(@PathVariable Long id) {
        Optional<Medicamento> medicamento = medicamentoService.getMedicamentoById(id);
            return medicamento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
}


    
}
