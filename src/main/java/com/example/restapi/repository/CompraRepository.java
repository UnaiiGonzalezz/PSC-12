package com.example.restapi.repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    List<Compra> findByClienteId(Long clienteId);   // EXISTENTE
    
    List<Compra> findByEstado(String estado);       // 

    List<Compra> findByCliente(Cliente cliente); 
}
