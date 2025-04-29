package com.example.restapi.repository;

import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovimientoRepository extends JpaRepository<StockMovimiento, Long> {

    // 🔵 Buscar movimientos por medicamento ordenados por fecha
    List<StockMovimiento> findByMedicamentoOrderByFechaDesc(Medicamento medicamento);

    // 🔵 BORRAR movimientos de stock relacionados a un medicamento 
    void deleteByMedicamento(Medicamento medicamento);
}
