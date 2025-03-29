package main.java.com.example.restapi.controller;

import main.java.com.example.restapi.model.Medicamento;
import main.java.com.example.restapi.service.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {
    
    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    public List<Medicamento> getAllMedicamentos() {
        return medicamentoService.getAllMedicamentos();
    }

    @PostMapping
    public Medicamento createMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.createMedicamento(medicamento);
    }
}
