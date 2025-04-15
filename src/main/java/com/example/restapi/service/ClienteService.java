package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> getClienteByTelefono(String telefono) {
        return Optional.ofNullable(clienteRepository.findByTelefono(telefono));
    }

    public Cliente saveCliente(Cliente cliente) {
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public boolean verificarCredenciales(String email, String contrasena) {
        Cliente cliente = clienteRepository.findByEmail(email);
        return cliente != null && passwordEncoder.matches(contrasena, cliente.getContrasena());
    }
}