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

    /* ✅ Verificar credenciales */
    public boolean verificarCredenciales(String email, String password) {
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        return clienteOpt.isPresent() && passwordEncoder.matches(password, clienteOpt.get().getContrasena());
    }

    /* ✅ Buscar cliente por email */
    public Cliente getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + email));
    }

    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    /* ✅ Verificar si un email ya existe */
    public boolean emailYaExiste(String email) {
        return clienteRepository.findByEmail(email).isPresent();
    }

    /* ✅ Registrar un cliente desde DTO */
    public Cliente registrarCliente(RegistroDTO registroDTO) {
        Cliente cliente = new Cliente();
        cliente.setNombre(registroDTO.getNombre());
        cliente.setApellido(registroDTO.getApellido());
        cliente.setEmail(registroDTO.getEmail());
        cliente.setTelefono(registroDTO.getTelefono());
        cliente.setMetodoPago(registroDTO.getMetodoPago() != null ? registroDTO.getMetodoPago() : "No especificado");
        cliente.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
        cliente.setRol("USER"); // 🔥 Siempre será USER
        return clienteRepository.save(cliente);
    }

    /* ✅ Guardar directamente un Cliente (ya creado) */
    public Cliente save(Cliente cliente) {
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        return clienteRepository.save(cliente);
    }

    /* ✅ Obtener todos los clientes */
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    /* ✅ Buscar cliente por ID */
    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    /* ✅ Actualizar datos de un cliente */
    public Cliente updateCliente(Long id, Cliente clienteDetails) {
        Cliente cliente = getClienteById(id);
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setMetodoPago(clienteDetails.getMetodoPago());
        cliente.setRol(clienteDetails.getRol());
        return clienteRepository.save(cliente);
    }

    /* ✅ Eliminar cliente */
    public boolean deleteCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            clienteRepository.delete(clienteOpt.get());
            return true;
        }
        return false;
    }
}
