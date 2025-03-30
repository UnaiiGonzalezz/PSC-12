package com.example.restapi.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.example.restapi.model.Compra;
import java.util.List;
 
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Buscar las compras por el estado en el que se encuentran (Pendiente, Enviado, Entregado, Cancelada)
    List<Compra> findByEstado(String estado);


}
