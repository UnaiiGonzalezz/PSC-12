package com.example.restapi.service;

import com.example.restapi.model.*;
import com.example.restapi.model.dto.CarritoDTO;
import com.example.restapi.model.dto.CheckoutResponseDTO;
import com.example.restapi.model.stock.MovimientoTipo;
import com.example.restapi.model.stock.StockMovimiento;
import com.example.restapi.repository.CarritoRepository;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.StockMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private StockMovimientoRepository movRepo;

    @Autowired
    private CompraService compraService;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    /* ---------- Utilidad interna ---------- */

    public Carrito getOrCreateCarrito(Cliente cliente) {
        return carritoRepository.findByCliente(cliente)
                .orElseGet(() -> carritoRepository.save(new Carrito(cliente)));
    }

    /* ---------- API pública ---------- */

    public CarritoDTO getCarritoDTO(Cliente cliente) {
        Carrito carrito = getOrCreateCarrito(cliente);

        var itemsDTO = carrito.getItems().stream()
                .map(ci -> new CarritoDTO.ItemDTO(
                        ci.getMedicamento().getId(),
                        ci.getMedicamento().getNombre(),
                        ci.getMedicamento().getPrecio(),
                        ci.getCantidad(),
                        ci.getSubtotal()))
                .collect(Collectors.toList());

        return new CarritoDTO(cliente.getId(), itemsDTO, carrito.getTotal());
    }

    public CarritoDTO addMedicamento(Cliente cliente, Long medicamentoId, int cantidad) {
        Carrito carrito = addMedicamento(cliente, medicamentoId, cantidad, true);
        return getCarritoDTO(cliente);
    }

    public CarritoDTO removeMedicamento(Cliente cliente, Long medicamentoId) {
        Carrito carrito = removeMedicamento(cliente, medicamentoId, true);
        return getCarritoDTO(cliente);
    }

    public void vaciarCarrito(Cliente cliente) {
        carritoRepository.findByCliente(cliente).ifPresent(carritoRepository::delete);
    }

    public double getTotal(Cliente cliente) {
        return getOrCreateCarrito(cliente).getTotal();
    }

    public CheckoutResponseDTO checkout(Cliente cliente) {
        Carrito carrito = carritoRepository.findByCliente(cliente)
                .orElseThrow(() -> new IllegalStateException("El carrito está vacío"));

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // 1. Verificar stock
        carrito.getItems().forEach(ci -> {
            Medicamento med = ci.getMedicamento();
            if (med.getStock() < ci.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para " + med.getNombre());
            }
        });

        // 2. Descontar stock y registrar movimiento
        carrito.getItems().forEach(ci -> {
            Medicamento med = ci.getMedicamento();
            int antes = med.getStock();
            med.setStock(antes - ci.getCantidad());
            if (med.getStock() == 0) {
                med.setDisponible(false);
            }
            medicamentoRepository.save(med);

            movRepo.save(new StockMovimiento(
                    med, MovimientoTipo.VENTA,
                    -ci.getCantidad(), antes, med.getStock()
            ));
        });

        // 3. Crear la compra
        var compra = compraService.crearDesdeCarrito(carrito);

        // 4. Vaciar carrito
        carritoRepository.delete(carrito);

        // 5. Mapear respuesta DTO
        var itemsDTO = compra.getMedicamentos().stream()
                .map(med -> new CheckoutResponseDTO.ItemDTO(
                        med.getNombre(),
                        med.getPrecio(),
                        1, // ← Ojo aquí: depende si manejas cantidad también en la compra
                        med.getPrecio())) // subtotal = precio * 1
                .collect(Collectors.toList());

        return new CheckoutResponseDTO(
                compra.getId(),
                compra.getFechaCompra(),
                compra.getEstado(),
                compra.getPago(),
                itemsDTO
        );
    }

    /* ---------- Métodos internos reutilizables ---------- */

    private Carrito addMedicamento(Cliente cliente, Long medicamentoId, int cantidad, boolean save) {
        Medicamento med = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));

        if (!med.isDisponible() || med.getStock() < cantidad) {
            throw new IllegalStateException("Medicamento sin stock suficiente");
        }

        Carrito carrito = getOrCreateCarrito(cliente);
        carrito.addItem(med, cantidad);
        return save ? carritoRepository.save(carrito) : carrito;
    }

    private Carrito removeMedicamento(Cliente cliente, Long medicamentoId, boolean save) {
        Carrito carrito = getOrCreateCarrito(cliente);
        carrito.removeItem(medicamentoId);
        return save ? carritoRepository.save(carrito) : carrito;
    }
}
