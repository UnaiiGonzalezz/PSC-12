package com.example.restapi.repository;

import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovimientoRepository extends JpaRepository<StockMovimiento, Long> {
    List<StockMovimiento> findByMedicamentoOrderByFechaDesc(Medicamento medicamento);
}