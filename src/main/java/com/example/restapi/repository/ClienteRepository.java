package com.example.restapi.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restapi.model.Cliente;
 
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
 
    // Buscar un cliente por su número de teléfono
    Cliente findByTelefono(String telefono);

    // Buscar un cliente por email
    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);

    
 
 }
