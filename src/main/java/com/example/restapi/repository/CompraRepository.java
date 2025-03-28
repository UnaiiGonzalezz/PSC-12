package main.java.com.example.restapi.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.example.restapi.model.Medicamento;
import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import java.util.List;
 
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
 
    // Buscar todas las compras de un medicamento
    List<Compra> findByMedicamento(Medicamento medicamento);

    // Buscar todas las compras de un cliente
    List<Compra> findByCliente(Cliente cliente);

    // Buscar las compras por el estado en el que se encuentran (Pendiente, Enviado, Entregado, Cancelada)
    List<Compra> findByEstado(String estado);


}
