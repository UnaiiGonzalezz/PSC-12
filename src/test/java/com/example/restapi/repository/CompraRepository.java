package com.example.restapi.repository;

import com.example.restapi.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByClienteId(Long clienteId);
    List<Compra> findByEstado(String estado);
}
