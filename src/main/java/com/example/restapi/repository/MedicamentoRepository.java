package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi.model.Medicamento;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Obtiene todos los medicamentos disponibles (disponible = true)
    List<Medicamento> findByDisponibleTrue();

    // Busca medicamentos por nombre, ignorando mayúsculas/minúsculas
    List<Medicamento> findByNombreIgnoreCase(String nombre);

    // Busca medicamentos por categoría, ignorando mayúsculas/minúsculas
    List<Medicamento> findByCategoriaIgnoreCase(String categoria);

    // Eliminado: findByNombre(String nombre), porque es redundante si ya tienes findByNombreIgnoreCase
}
