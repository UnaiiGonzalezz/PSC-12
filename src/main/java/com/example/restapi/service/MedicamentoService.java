package com.example.restapi.service;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.restapi.model.stock.*;
import com.example.restapi.repository.StockMovimientoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;
    @Autowired 
    private StockMovimientoRepository movRepo;

    /* ---------- CRUD básico ---------- */

    public List<Medicamento> getAllMedicamentos() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> getMedicamentoById(Long id) {
        return medicamentoRepository.findById(id);
    }

    public List<Medicamento> getMedicamentosDisponibles() {
        return medicamentoRepository.findByDisponibleTrue();
    }

     /* ---------- guardar ---------- */
    public Medicamento saveMedicamento(Medicamento med) {
        Medicamento guardado = medicamentoRepository.save(med);
        // registrar ENTRADA
        movRepo.save(new StockMovimiento(
                guardado, MovimientoTipo.ENTRADA,
                guardado.getStock(), 0, guardado.getStock()));
        return guardado;
    }


    /* ---------- NUEVO: paginación ---------- */
    public Page<MedicamentoDTO> getMedicamentos(Pageable pageable) {
        return medicamentoRepository.findAll(pageable)
                .map(m -> new MedicamentoDTO(m.getId(), m.getNombre(), m.getPrecio()));
    }

    /* ---------- actualizar ---------- */
    /* ---------- actualizar ---------- */
    public Medicamento updateMedicamento(Long id, Medicamento datos) {
        Medicamento existente = medicamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

        int antes = existente.getStock();
        int despues = datos.getStock();
        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(despues);
        existente.setProveedor(datos.getProveedor());
        existente.setDisponible(datos.isDisponible());

        Medicamento actualizado = medicamentoRepository.save(existente);

        if (antes != despues) { // registrar ajuste
            movRepo.save(new StockMovimiento(actualizado, MovimientoTipo.AJUSTE,
                    despues - antes, antes, despues));
        }
        return actualizado;
    }

    public void deleteMedicamento(Long id) {
        medicamentoRepository.deleteById(id);
    }

    /* ---------- búsquedas ---------- */
    public List<Medicamento> getMedicamentosPorNombre(String nombre) {
        return medicamentoRepository.findByNombreIgnoreCase(nombre);
    }

    public List<Medicamento> getMedicamentosPorCategoria(String categoria) {
        return medicamentoRepository.findByCategoriaIgnoreCase(categoria);
    }
}
