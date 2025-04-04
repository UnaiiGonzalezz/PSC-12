package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Buscar cliente por ID
    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    // Buscar cliente por teléfono
    public Optional<Cliente> getClienteByTelefono(String telefono) {
        return Optional.ofNullable(clienteRepository.findByTelefono(telefono));
    }

    // Guardar o actualizar un cliente
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Eliminar un cliente por ID
    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    /*public boolean verificarCredenciales(String email, String contrasena) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente != null && cliente.getContrasena().equals(contrasena)) {
            return true;
        }
        return false;
    }*/

}

