package com.example.restapi.repository;

import com.example.restapi.model.Carrito;
import com.example.restapi.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /** Devuelve el carrito actual del cliente (si existe). */
    Optional<Carrito> findByCliente(Cliente cliente);
}
