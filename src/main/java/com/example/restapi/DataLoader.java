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
            // Crear clientes con contraseñas encriptadas
            Cliente cliente1 = new Cliente(
                "Domingo", 
                "Bermejo", 
                "domingo@bermejo.com", 
                passwordEncoder.encode("123"), 
                "123456789", 
                "Tarjeta"
            );
            
            Cliente cliente2 = new Cliente(
                "Ana", 
                "Gómez", 
                "ana@gomez.com", 
                passwordEncoder.encode("456"), 
                "987654321", 
                "PayPal"
            );
            
            clienteRepository.saveAll(List.of(cliente1, cliente2));

            // Crear medicamentos de prueba
            Medicamento med1 = new Medicamento("Paracetamol", "Analgésico", 10.0, 100, "Proveedor A");
            Medicamento med2 = new Medicamento("Ibuprofeno", "Antiinflamatorio", 12.5, 200, "Proveedor B");
            Medicamento med3 = new Medicamento("Amoxicilina", "Antibiótico", 15.0, 150, "Proveedor C");
            medicamentoRepository.saveAll(List.of(med1, med2, med3));

            // Crear compras de prueba
            Compra compra1 = new Compra(
                cliente1, 
                List.of(med1, med2), 
                LocalDate.now(), 
                2, 
                "Tarjeta"
            );
            
            Compra compra2 = new Compra(
                cliente2, 
                List.of(med2, med3), 
                LocalDate.now().minusDays(1), 
                1, 
                "Efectivo"
            );
            
            compraRepository.saveAll(List.of(compra1, compra2));

            System.out.println("Datos iniciales cargados correctamente.");
        };
    }
}