package com.example.restapi.service;

import com.example.restapi.model.Compra;
import com.example.restapi.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    // Obtener todas las compras
    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    // Buscar compra por ID
    public Optional<Compra> getCompraById(Long id) {
        return compraRepository.findById(id);
    }

    // Buscar compras por estado
    public List<Compra> getComprasByEstado(String estado) {
        return compraRepository.findByEstado(estado);
    }

    // Guardar o actualizar una compra
    public Compra saveCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    // Eliminar una compra por ID
    public void deleteCompra(Long id) {
        compraRepository.deleteById(id);
    }
}

