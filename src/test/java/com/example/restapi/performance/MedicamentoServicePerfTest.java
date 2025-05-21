package com.example.restapi.performance;

import com.example.restapi.service.MedicamentoService;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.CsvReportGenerator;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("performance")
public class MedicamentoServicePerfTest {

    @Autowired
    private MedicamentoService medicamentoService;

    /*@BeforeAll
    static void setupReporter() {
        JUnitPerfTestReporterProvider.setReportGenerator(
            new CompositeReportGenerator(
                new HtmlReportGenerator("target/perf-medicamento.html"),
                new JsonReportGenerator("target/perf-medicamento.json"),
                new CsvReportGenerator("target/perf-medicamento.csv")
            )
        );
    }*/

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(meanLatency = 200, maxLatency = 500, percentiles = "95:300")
    void testBuscarTodosLosMedicamentosPerformance() {
        medicamentoService.getAllMedicamentos();
    }
}
