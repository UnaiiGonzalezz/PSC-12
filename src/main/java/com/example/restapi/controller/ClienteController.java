package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.ClienteDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Endpoint para obtener todos los clientes como DTO
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();
        List<ClienteDTO> clienteDTOs = clientes.stream()
            .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getApellido(), cliente.getEmail(), cliente.getMetodoPago()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(clienteDTOs);  // Retorna la lista de DTOs
    }

    // Endpoint para obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        if (cliente != null) {
            ClienteDTO clienteDTO = new ClienteDTO(cliente.getId(), cliente.getNombre(), cliente.getApellido(), cliente.getEmail(), cliente.getMetodoPago());
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.notFound().build();  // Si el cliente no existe
        }
    }

    // Endpoint para registrar un nuevo cliente
    @PostMapping
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody RegistroDTO registroDTO) {
        Cliente nuevoCliente = clienteService.registrarCliente(registroDTO);
        ClienteDTO clienteDTO = new ClienteDTO(nuevoCliente.getId(), nuevoCliente.getNombre(), nuevoCliente.getApellido(), nuevoCliente.getEmail(), nuevoCliente.getMetodoPago());
        return ResponseEntity.ok(clienteDTO);
    }

    // Endpoint para actualizar los datos de un cliente
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.updateCliente(id, cliente);
        if (clienteActualizado != null) {
            ClienteDTO clienteDTO = new ClienteDTO(clienteActualizado.getId(), clienteActualizado.getNombre(), clienteActualizado.getApellido(), clienteActualizado.getEmail(), clienteActualizado.getMetodoPago());
            return ResponseEntity.ok(clienteDTO);
        } else {
            return ResponseEntity.notFound().build();  // Si no se encuentra el cliente
        }
    }

    // Endpoint para eliminar un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.deleteCliente(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();  // El cliente ha sido eliminado correctamente
        } else {
            return ResponseEntity.notFound().build();  // Si no se encuentra el cliente
        }
    }
}
