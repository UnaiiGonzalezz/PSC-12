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
import java.util.ArrayList;
import java.util.Random;

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

            // Generar MUCHOS clientes extra
            List<Cliente> extraClients = new ArrayList<>();
            String[] nombres = {"María", "Carlos", "Ana", "John", "Laura", "Pedro", "Sofía", "Miguel", "Lucía", "David", "Daniela", "Sergio", "Elena", "Manuel", "Sara", "Javier", "Paula", "Alberto", "Marta", "Iván"};
            String[] apellidos = {"Pérez", "Gómez", "López", "Doe", "Martín", "Ruiz", "Fernández", "Sánchez", "Ramírez", "Vargas", "Moreno", "Castro", "Navarro", "Ortega", "Delgado", "Silva", "Torres", "Molina", "Suárez", "Vega"};
            String[] pagos = {"Tarjeta", "Transferencia", "Bizum", "Efectivo"};
            Random rand = new Random();

            int totalClientesExtra = 80; // + los que ya tienes, serán 100
            for (int i = 0; i < totalClientesExtra; i++) {
                String nombre = nombres[rand.nextInt(nombres.length)];
                String apellido = apellidos[rand.nextInt(apellidos.length)];
                String email = (nombre + "." + apellido + i + "@mail.com").toLowerCase();
                String telefono = "6" + (rand.nextInt(100000000) + 10000000);
                String metodoPago = pagos[rand.nextInt(pagos.length)];
                Cliente c = new Cliente(
                        nombre,
                        apellido,
                        email,
                        passwordEncoder.encode("password" + (100 + i)),
                        telefono,
                        metodoPago,
                        "USER"
                );
                extraClients.add(c);
            }

            extraClients.forEach(c ->
                    clienteRepository.findByEmail(c.getEmail())
                            .orElseGet(() -> clienteRepository.save(c))
            );

            // ---------------------------------------------------------------------
            // 2. CARGA DE MEDICAMENTOS
            // ---------------------------------------------------------------------
            List<Medicamento> catalogo = new ArrayList<>(List.of(
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
            ));

            // GENERAR MÁS DE 120 MEDICAMENTOS EXTRAS (nombre único, categoría, precios, stocks, laboratorio aleatorio)
            String[] bases = {"Cetirizina", "Desloratadina", "Ranitidina", "Azitromicina", "Lorazepam", "Fluoxetina", "Escitalopram", "Tramadol", "Doxiciclina", "Claritromicina", "Enalapril", "Carvedilol", "Rosuvastatina", "Lamotrigina", "Vildagliptina", "Tamsulosina", "Topiramato", "Mirtazapina", "Baclofeno", "Montelukast"};
            String[] formas = {"Comprimidos", "Jarabe", "Inyectable", "Óvulos", "Pomada", "Spray", "Gotas", "Suspensión", "Crema", "Polvo"};
            String[] categorias = {"Antialérgico", "Antibiótico", "Ansiolítico", "Antidepresivo", "Analgésico", "Antiinflamatorio", "Corticoide", "Broncodilatador", "Diurético", "Hipoglucemiante", "Anticonvulsivo", "Colesterol", "Antiulceroso", "Vasodilatador"};
            String[] labs = {"Normon", "Pfizer", "Teva", "Cinfa", "Bayer", "Sandoz", "Sanofi", "GSK", "Almirall", "Novartis", "Merck"};
            
            int totalMedicamentosExtra = 150;
            for (int i = 0; i < totalMedicamentosExtra; i++) {
                String base = bases[rand.nextInt(bases.length)];
                String forma = formas[rand.nextInt(formas.length)];
                String dosis = (rand.nextInt(8) + 1) * 10 + " mg";
                String nombre = base + " " + dosis + " " + forma + " " + (i+1);
                String categoria = categorias[rand.nextInt(categorias.length)];
                double precio = Math.round((1 + rand.nextDouble() * 15) * 100.0) / 100.0;
                int stock = rand.nextInt(700) + 50;
                String lab = labs[rand.nextInt(labs.length)];
                Medicamento m = new Medicamento(nombre, categoria, precio, stock, lab);
                catalogo.add(m);
            }

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
