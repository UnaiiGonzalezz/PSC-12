package com.example.restapi.service;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean verificarCredenciales(String email, String password) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        return clienteOpt.isPresent() && passwordEncoder.matches(password, clienteOpt.get().getContrasena());
    }

    public Cliente getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente registrarCliente(RegistroDTO registroDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(registroDTO.getNombre());
        cliente.setApellido(registroDTO.getApellido());
        cliente.setEmail(registroDTO.getEmail());
        cliente.setTelefono(registroDTO.getTelefono());
        cliente.setMetodoPago(registroDTO.getMetodoPago());
        cliente.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    public Cliente updateCliente(Long id, Cliente clienteDetails) {
        Cliente cliente = getClienteById(id);
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setMetodoPago(clienteDetails.getMetodoPago());
        return clienteRepository.save(cliente);
    }

    public void deleteCliente(Long id) {
        Cliente cliente = getClienteById(id);
        clienteRepository.delete(cliente);
    }
}
