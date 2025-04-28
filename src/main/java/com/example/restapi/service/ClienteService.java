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

    /**
     * Registrar un nuevo cliente
     * @param registroDTO DTO con los datos del nuevo cliente
     * @return El cliente recién registrado
     */
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

    /**
     * Actualizar los datos de un cliente existente
     * @param id El id del cliente a actualizar
     * @param clienteDetails El objeto con los nuevos detalles del cliente
     * @return El cliente actualizado
     */
    public Cliente updateCliente(Long id, Cliente clienteDetails) {
        Cliente cliente = getClienteById(id);
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setMetodoPago(clienteDetails.getMetodoPago());
        return clienteRepository.save(cliente);
    }

    /**
     * Eliminar un cliente por su ID
     * @param id El id del cliente a eliminar
     * @return true si el cliente fue eliminado correctamente, false si no fue encontrado
     */
    public boolean deleteCliente(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            clienteRepository.delete(clienteOpt.get());
            return true;  // Cliente eliminado con éxito
        }
        return false;  // No se encontró el cliente con el ID proporcionado
    }
}
