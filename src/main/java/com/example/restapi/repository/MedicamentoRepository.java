package com.example.restapi.repository;

import com.example.restapi.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Medicamentos marcados como disponibles
    List<Medicamento> findByDisponibleTrue();

    // Búsqueda insensible a mayúsculas/minúsculas por nombre exacto
    List<Medicamento> findByNombreIgnoreCase(String nombre);

    // Búsqueda insensible por categoría exacta
    List<Medicamento> findByCategoriaIgnoreCase(String categoria);
}
