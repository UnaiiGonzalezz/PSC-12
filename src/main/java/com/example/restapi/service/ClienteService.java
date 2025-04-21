package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Compra;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CompraRepository compraRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, CompraRepository compraRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Cliente registrarCliente(RegistroDTO registroDTO) {
        if (clienteRepository.existsByEmail(registroDTO.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        Cliente cliente = new Cliente(
            registroDTO.getNombre(),
            registroDTO.getApellido(),
            registroDTO.getEmail(),
            passwordEncoder.encode(registroDTO.getContrasena()),
            registroDTO.getTelefono(),
            registroDTO.getMetodoPago()
        );
        
        return clienteRepository.save(cliente);
    }

    public List<Compra> obtenerComprasPorCliente(Long clienteId) {
        return compraRepository.findByClienteId(clienteId);
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