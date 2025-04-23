// src/main/java/com/example/restapi/service/CompraService.java
package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.EstadoCompraDTO;
import com.example.restapi.model.dto.CompraResumenDTO;
import com.example.restapi.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    /* ---------- CRUD básico ---------- */
    public List<Compra> getAllCompras() { return compraRepository.findAll(); }
    public Optional<Compra> getCompraById(Long id) { return compraRepository.findById(id); }
    public List<Compra> getComprasByEstado(String e) { return compraRepository.findByEstado(e); }
    public Compra saveCompra(Compra c) { return compraRepository.save(c); }
    public void deleteCompra(Long id) { compraRepository.deleteById(id); }

    /* ---------- NUEVO: cambiar estado ---------- */
    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("Pendiente", "Enviado", "Entregado", "Cancelada");

    public Compra updateEstado(Long id, String nuevoEstado) {
        if (!ESTADOS_VALIDOS.contains(nuevoEstado))
            throw new IllegalArgumentException("Estado no válido");

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));

        compra.setEstado(nuevoEstado);
        return compraRepository.save(compra);
    }

    /* ---------- Historial por cliente ---------- */
    public List<CompraResumenDTO> getHistorialPorCliente(Long clienteId) {
        return compraRepository.findByClienteId(clienteId).stream()
                .map(c -> new CompraResumenDTO(c.getId(), c.getFechaCompra(),
                        c.getEstado(), c.getPago()))
                .collect(Collectors.toList());
    }

    /* ---------- Estado detallado ---------- */
    public Optional<EstadoCompraDTO> getEstadoCompraDTO(Long id) {
        return compraRepository.findById(id).map(this::mapToEstadoDTO);
    }

    private EstadoCompraDTO mapToEstadoDTO(Compra c) {
        var clienteInfo = new EstadoCompraDTO.ClienteInfo(
                c.getCliente().getNombre(), c.getCliente().getApellido(), c.getCliente().getEmail());

        var medsInfo = c.getMedicamentos().stream()
                .map(m -> new EstadoCompraDTO.MedicamentoInfo(m.getNombre(), m.getPrecio()))
                .collect(Collectors.toList());

        return new EstadoCompraDTO(c.getId(), c.getEstado(), c.getFechaCompra(),
                clienteInfo, medsInfo);
    }

    /* ---------- crear desde carrito (ya existente) ---------- */
    @Transactional
    public Compra crearDesdeCarrito(Carrito carrito) {
        Cliente cliente = carrito.getCliente();
        int totalUnidades = carrito.getItems().stream().mapToInt(CarritoItem::getCantidad).sum();
        List<Medicamento> meds = carrito.getItems().stream()
                .map(CarritoItem::getMedicamento).collect(Collectors.toList());

        Compra compra = new Compra(cliente, meds,
                LocalDate.now(), totalUnidades, cliente.getMetodoPago());

        double pago = carrito.getItems().stream()
                .mapToDouble(CarritoItem::getSubtotal).sum();
        compra.setPago(pago);

        return compraRepository.save(compra);
    }
}
