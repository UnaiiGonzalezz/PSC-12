package com.example.restapi.service;

import com.example.restapi.model.Medicamento;
import com.example.restapi.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    // Obtener todos los medicamentos
    public List<Medicamento> getAllMedicamentos() {
        return medicamentoRepository.findAll();
    }

    // Buscar medicamento por ID
    public Optional<Medicamento> getMedicamentoById(Long id) {
        return medicamentoRepository.findById(id);
    }

    // Obtener los medicamentos disponibles
    public List<Medicamento> getMedicamentosDisponibles() {
        return medicamentoRepository.findByDisponibleTrue();
    }

    // Guardar o actualizar un medicamento
    public Medicamento saveMedicamento(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    // Eliminar un medicamento por ID
    public void deleteMedicamento(Long id) {
        medicamentoRepository.deleteById(id);
    }

    // Nuevo método para buscar por nombre
    public List<Medicamento> getMedicamentosPorNombre(String nombre) {
        return medicamentoRepository.findByNombreIgnoreCase(nombre);
    }

    // Nuevo método para buscar por categoría
    public List<Medicamento> getMedicamentosPorCategoria(String categoria) {
        return medicamentoRepository.findByCategoriaIgnoreCase(categoria);
    }
}

