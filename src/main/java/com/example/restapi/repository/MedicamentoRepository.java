package com.example.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restapi.model.Medicamento;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Método que obtiene los medicamentos que están disponibles
    List<Medicamento> findByDisponibleTrue();

    // Método para buscar medicamentos por nombre ignorando mayúsculas y minúsculas
    List<Medicamento> findByNombreIgnoreCase(String nombre);

    // Método para buscar medicamentos por categoría ignorando mayúsculas y minúsculas
    List<Medicamento> findByCategoriaIgnoreCase(String categoria);

    // ⚡️ Ahora devuelve una LISTA en vez de Optional único
    List<Medicamento> findByNombre(String nombre);

    
}
