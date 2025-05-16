package com.example.restapi;

import com.example.restapi.model.Cliente;
import com.example.restapi.model.Medicamento;
import com.example.restapi.model.Compra;
import com.example.restapi.repository.ClienteRepository;
import com.example.restapi.repository.MedicamentoRepository;
import com.example.restapi.repository.CompraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            ClienteRepository clienteRepository,
            MedicamentoRepository medicamentoRepository,
            CompraRepository compraRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // ---------------------------------------------------------------------
            // 1. CARGA DE CLIENTES
            // ---------------------------------------------------------------------
            // Siempre aseguramos que existan al menos un administrador y un usuario
            Cliente admin = clienteRepository.findByEmail("admin@farmacia.com")
                    .orElseGet(() -> clienteRepository.save(
                            new Cliente(
                                    "Admin",
                                    "Farmacia",
                                    "admin@farmacia.com",
                                    passwordEncoder.encode("admin123"),
                                    "111222333",
                                    "Transferencia",
                                    "ADMIN"
                            )));

            Cliente user = clienteRepository.findByEmail("cliente@farmacia.com")
                    .orElseGet(() -> clienteRepository.save(
                            new Cliente(
                                    "Cliente",
                                    "Normal",
                                    "cliente@farmacia.com",
                                    passwordEncoder.encode("user123"),
                                    "444555666",
                                    "Tarjeta",
                                    "USER"
                            )));

            // --- Clientes adicionales (solo si no existen) ---
            List<Cliente> extraClients = List.of(
                    new Cliente("María", "Pérez", "maria.perez@example.com", passwordEncoder.encode("password123"), "600111222", "Tarjeta", "USER"),
                    new Cliente("Carlos", "Gómez", "carlos.gomez@example.com", passwordEncoder.encode("password123"), "600333444", "Efectivo", "USER"),
                    new Cliente("Ana", "López", "ana.lopez@example.com", passwordEncoder.encode("password123"), "600555666", "Transferencia", "USER"),
                    new Cliente("John", "Doe", "john.doe@example.com", passwordEncoder.encode("password123"), "600777888", "Tarjeta", "USER"),
                    new Cliente("Laura", "Martín", "laura.martin@example.com", passwordEncoder.encode("password123"), "600999000", "Bizum", "USER")
            );

            extraClients.forEach(c ->
                    clienteRepository.findByEmail(c.getEmail())
                            .orElseGet(() -> clienteRepository.save(c))
            );

            // ---------------------------------------------------------------------
            // 2. CARGA DE MEDICAMENTOS
            // ---------------------------------------------------------------------
            // Lista grande de medicamentos de ejemplo
            List<Medicamento> catalogo = List.of(
                    new Medicamento("Paracetamol", "Analgésico", 3.50, 500, "Kern Pharma"),
                    new Medicamento("Ibuprofeno", "Antiinflamatorio", 4.20, 400, "Cinfa"),
                    new Medicamento("Amoxicilina 500 mg", "Antibiótico", 4.75, 300, "Sandoz"),
                    new Medicamento("Omeprazol 20 mg", "Inhibidor de la bomba de protones", 3.10, 250, "Normon"),
                    new Medicamento("Amlodipino 10 mg", "Antihipertensivo", 2.25, 200, "Pfizer"),
                    new Medicamento("Metformina 850 mg", "Antidiabético", 2.80, 350, "Teva"),
                    new Medicamento("Simvastatina 40 mg", "Reductor de colesterol", 1.95, 180, "Bayer"),
                    new Medicamento("Losartán 50 mg", "Antihipertensivo", 2.50, 220, "Merck"),
                    new Medicamento("Levotiroxina 100 µg", "Hormona tiroidea", 1.60, 160, "Sanofi"),
                    new Medicamento("Ácido Acetilsalicílico 100 mg", "Antiagregante", 1.25, 600, "Bayer"),
                    new Medicamento("Clopidogrel 75 mg", "Antiagregante", 5.60, 140, "Sanofi"),
                    new Medicamento("Atorvastatina 20 mg", "Estatina", 3.95, 210, "Pfizer"),
                    new Medicamento("Metronidazol 500 mg", "Antibiótico", 2.90, 190, "Normon"),
                    new Medicamento("Diclofenaco 50 mg", "Antiinflamatorio", 3.20, 280, "Novartis"),
                    new Medicamento("Salbutamol Inhalador", "Broncodilatador", 6.50, 120, "GSK"),
                    new Medicamento("Lisinopril 20 mg", "Antihipertensivo", 2.35, 230, "Teva"),
                    new Medicamento("Furosemida 40 mg", "Diurético", 1.80, 260, "Sanofi"),
                    new Medicamento("Prednisona 30 mg", "Corticoide", 3.45, 150, "Almirall"),
                    new Medicamento("Ciprofloxacino 500 mg", "Antibiótico", 4.10, 175, "Bayer"),
                    new Medicamento("Gabapentina 300 mg", "Antiepiléptico", 5.25, 130, "Pfizer")
            );

            catalogo.forEach(m ->
                    medicamentoRepository.findByNombreIgnoreCase(m.getNombre())
                            .stream()
                            .findFirst()
                            .orElseGet(() -> medicamentoRepository.save(m))
            );

            // ---------------------------------------------------------------------
            // 3. COMPRA DE PRUEBA (solo si no hay ninguna)
            // ---------------------------------------------------------------------
            if (compraRepository.count() == 0) {
                Compra compra1 = new Compra(
                        user,
                        List.of(
                                medicamentoRepository.findByNombreIgnoreCase("Paracetamol").get(0),
                                medicamentoRepository.findByNombreIgnoreCase("Ibuprofeno").get(0)
                        ),
                        LocalDate.now(),
                        2,
                        "Tarjeta"
                );
                compraRepository.save(compra1);
            }

            System.out.println("✅ Datos iniciales cargados correctamente.");
        };
    }
}
