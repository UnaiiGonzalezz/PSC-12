package com.example.restapi.controller;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.dto.ClienteDTO;
import com.example.restapi.model.dto.RegistroDTO;
import com.example.restapi.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /* ---------- util ---------- */
    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(c.getId(), c.getNombre(), c.getApellido(),
                              c.getEmail(), c.getMetodoPago());
    }

    /* ---------- #30 Registro ---------- */
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO registrar(@RequestBody RegistroDTO body) {
        return toDTO(clienteService.registrarCliente(body));
    }

    /* ---------- #31 Listado ---------- */
    @GetMapping
    public List<ClienteDTO> list() {
        return clienteService.getAllClientes().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteDTO get(@PathVariable Long id) {
        return clienteService.getClienteById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    /* ---------- #32 Actualizar ---------- */
    @PutMapping("/{id}")
    public ClienteDTO update(@PathVariable Long id, @RequestBody Cliente body) {
        return toDTO(clienteService.updateCliente(id, body));
    }

    /* ---------- #33 Eliminar ---------- */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clienteService.deleteCliente(id);
    }
}
