package com.example.restapi.service;

import com.example.restapi.model.Medicamento;
import com.example.restapi.model.dto.MedicamentoDTO;
import com.example.restapi.model.stock.MovimientoTipo;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private StockMovimientoRepository movRepo;

    public List<Medicamento> getAllMedicamentos() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> getMedicamentoById(Long id) {
        return medicamentoRepository.findById(id);
    }

    public List<MedicamentoDTO> getMedicamentosDisponibles() {
        return medicamentoRepository.findByDisponibleTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Medicamento saveMedicamento(Medicamento med) {
        Medicamento guardado = medicamentoRepository.save(med);
        movRepo.save(new StockMovimiento(
                guardado,
                MovimientoTipo.ENTRADA,
                guardado.getStock(),
                0,
                guardado.getStock()
        ));
        return guardado;
    }

    public Page<MedicamentoDTO> getMedicamentos(Pageable pageable) {
        return medicamentoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Medicamento updateMedicamento(Long id, Medicamento datos) {
        Medicamento existente = medicamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

        int stockAntes = existente.getStock();
        int stockDespues = datos.getStock();

        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(stockDespues);
        existente.setProveedor(datos.getProveedor());
        existente.setDisponible(datos.isDisponible());

        Medicamento actualizado = medicamentoRepository.save(existente);

        if (stockAntes != stockDespues) {
            movRepo.save(new StockMovimiento(
                    actualizado,
                    MovimientoTipo.AJUSTE,
                    stockDespues - stockAntes,
                    stockAntes,
                    stockDespues
            ));
        }
        return actualizado;
    }

    public void deleteMedicamento(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

        movRepo.deleteByMedicamento(medicamento);
        medicamentoRepository.delete(medicamento);
    }

    public List<Medicamento> getMedicamentosPorNombre(String nombre) {
        return medicamentoRepository.findByNombreIgnoreCase(nombre);
    }

    public List<Medicamento> getMedicamentosPorCategoria(String categoria) {
        return medicamentoRepository.findByCategoriaIgnoreCase(categoria);
    }

    public List<StockMovimiento> getMovimientosDeMedicamento(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
        return movRepo.findByMedicamentoOrderByFechaDesc(medicamento);
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
