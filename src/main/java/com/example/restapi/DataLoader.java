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
            // Insertar clientes (si no existen)
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
                            )
                    ));

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
                            )
                    ));

            // Insertar medicamentos (si no existen)
            Medicamento med1 = medicamentoRepository.findByNombreIgnoreCase("Paracetamol")
                    .stream()
                    .findFirst()
                    .orElseGet(() -> medicamentoRepository.save(
                            new Medicamento("Paracetamol", "Analgésico", 10.0, 100, "Proveedor A")
                    ));

            Medicamento med2 = medicamentoRepository.findByNombreIgnoreCase("Ibuprofeno")
                    .stream()
                    .findFirst()
                    .orElseGet(() -> medicamentoRepository.save(
                            new Medicamento("Ibuprofeno", "Antiinflamatorio", 12.5, 200, "Proveedor B")
                    ));

            // Insertar compras de prueba (si no existen)
            if (compraRepository.count() == 0) {
                Compra compra1 = new Compra(
                        user,
                        List.of(med1, med2),
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
