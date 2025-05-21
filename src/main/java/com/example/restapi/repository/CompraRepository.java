package com.example.restapi.repository;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByClienteId(Long clienteId);

    List<Compra> findByEstado(String estado);

    List<Compra> findByCliente(Cliente cliente);

    List<Compra> findByClienteEmail(String email);

    List<Compra> findByClienteEmailAndEstado(String email, String estado);

    // --- Métodos con fetch join ---
    @Query("SELECT DISTINCT c FROM Compra c JOIN FETCH c.medicamentos WHERE c.cliente.email = :email")
    List<Compra> findWithMedicamentosByClienteEmail(@Param("email") String email);

    @Query("SELECT DISTINCT c FROM Compra c JOIN FETCH c.medicamentos WHERE c.cliente.email = :email AND UPPER(c.estado) = UPPER(:estado)")
    List<Compra> findWithMedicamentosByClienteEmailAndEstado(@Param("email") String email, @Param("estado") String estado);
}
