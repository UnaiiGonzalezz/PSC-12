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
public class BookServicePerformanceWithReportTest {

    // Inyección del servicio que se quiere medir
    @Autowired
    private BookService bookService;

    // Objetos para generar el reporte y recolectar estadísticas
    private HtmlReportGenerator reportGenerator;
    private StatisticsCollector collector;

    // Inicialización antes de todos los tests
    @BeforeAll
    public void setup() {
        // Crear el recolector de estadísticas con un nombre personalizado
        collector = new StatisticsCollector("BookService Performance Report");

        // Establecer la ruta donde se generará el reporte HTML
        reportGenerator = new HtmlReportGenerator(new File("target/junitperf-book-report.html"));
    }

    // Test que ejecuta el método múltiples veces y mide el rendimiento
    @Test
    public void testGetAllBooksWithReport() {
        // Repetir 100 veces para obtener una muestra representativa
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            try {
                // Ejecutar el método que se desea medir
                bookService.getAllBooks();

                // Registrar ejecución exitosa y su duración
                collector.recordSuccessEvent(System.currentTimeMillis() - start);
            } catch (Exception e) {
                // Registrar ejecución fallida y su duración
                collector.recordErrorEvent(System.currentTimeMillis() - start);
            }
        }
    }

    // Después de todos los tests, generar el reporte HTML
    @AfterAll
    public void writeReport() {
        reportGenerator.writeReport(collector);
    }
}
*/
