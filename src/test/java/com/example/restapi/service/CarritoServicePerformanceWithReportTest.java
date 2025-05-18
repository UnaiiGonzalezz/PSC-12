/* 
package com.example.restapi.service;

import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.github.noconnor.junitperf.statistics.StatisticsCollector;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag(name = "service")
public class CarritoServicePerformanceWithReportTest {

    @Autowired
    private CarritoService carritoService;

    private HtmlReportGenerator reportGenerator;
    private StatisticsCollector collector;

    @BeforeAll
    public void setup() {
        collector = new StatisticsCollector("CarritoService Performance Report");
        reportGenerator = new HtmlReportGenerator(new File("target/junitperf-carrito-report.html"));
    }

    @Test
    public void testObtenerCarritoWithReport() {
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            try {
                carritoService.obtenerCarrito(); // Asegúrate que este método exista
                collector.recordSuccessEvent(System.currentTimeMillis() - start);
            } catch (Exception e) {
                collector.recordErrorEvent(System.currentTimeMillis() - start);
            }
        }
    }

    @AfterAll
    public void writeReport() {
        reportGenerator.writeReport(collector);
    }
}
*/