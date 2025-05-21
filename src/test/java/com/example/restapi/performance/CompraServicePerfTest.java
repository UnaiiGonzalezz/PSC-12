package com.example.restapi.performance;

import com.example.restapi.model.*;
import com.example.restapi.service.CompraService;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.CsvReportGenerator;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Tag("performance")
public class CompraServicePerfTest {

    @Autowired
    private CompraService compraService;

    /*@BeforeAll
    static void setupReporter() {
        JUnitPerfTestReporterProvider.setReportGenerator(
            new CompositeReportGenerator(
                new HtmlReportGenerator("target/perf-compra.html"),
                new JsonReportGenerator("target/perf-compra.json"),
                new CsvReportGenerator("target/perf-compra.csv")
            )
        );
    }*/

    @Test
    @JUnitPerfTest(threads = 3, durationMs = 3000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(meanLatency = 200, maxLatency = 700, percentiles = "95:500")
    void testCrearDesdeCarritoPerformance() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setMetodoPago("Efectivo");

        Medicamento medicamento = new Medicamento();
        medicamento.setId(1L);
        medicamento.setNombre("Paracetamol");
        medicamento.setPrecio(10.0);

        CarritoItem item = new CarritoItem();
        item.setMedicamento(medicamento);
        item.setCantidad(2);

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carrito.setItems(List.of(item));

        compraService.crearDesdeCarrito(carrito);
    }
}
